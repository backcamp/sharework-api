package com.sharework.service;

import com.sharework.common.ApplicationTypeEnum;
import com.sharework.common.JobTypeEnum;
import com.sharework.dao.*;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.request.model.APIApplicationApplied;
import com.sharework.response.model.Coordinate;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.Response;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.application.APIApplicationHistory;
import com.sharework.response.model.application.APIApplicationStatusOverview;
import com.sharework.response.model.application.APIReceiptWorker;
import com.sharework.response.model.job.JobOverview;
import com.sharework.response.model.job.JobTagList;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.user.Giver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    ApplicationDao applicationDao;
    @Autowired
    ApplicationChecklistDao applicationChecklistDao;
    @Autowired
    JobDao jobDao;
    @Autowired
    JobTagDao jobTagDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TokenIdentification identification;

    @Autowired
    ApplicationTotalPaymentDao applicationTotalPaymentDao;
    private int PAGE_SIZE = 100;

    public ResponseEntity insertApplication(APIApplicationApplied application, String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        // 이미 지원한 경우
        if (applicationDao.findByJobIdAndUserId(application.getJobId(), userId) != null) {
            String errorMsg = "이미 지원한 공고입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            // FIXME - 예외 핸들링하는 부분 controller advice 사용해, 가장 바깥에서 일괄 처리하도록 수정
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        Optional<Job> job = jobDao.findById(application.getJobId());

        // application 저장
        Application insertApplication = applicationDao.save(Application.builder().userId(userId).jobId(application.getJobId()).lat(application.getLat()).lng(application.getLng()).status(ApplicationTypeEnum.APPLIED.name()).startAt(job.get().getStartAt()).endAt(job.get().getEndAt()).build());

        // 선택한 checklist 저장
        List<Integer> applicationChecklistIds = application.getApplicationChecklistIds();
        for (int checklistId : applicationChecklistIds) {
            applicationChecklistDao.save(ApplicationChecklist.builder().applicationId(insertApplication.getId()).jobChecklistId(checklistId).build());
        }

        // FIXME - Response entity를 만드는 책임은 service가 아닌 controller에서 있다. Response entity 만드는 부분 모두 controller로 옮기기.
        response = new ResponseEntity<>(new Response(new BasicMeta(true, "")), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getApplicationList(String status, int page, String accessToken) {

        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Application> applications = applicationDao.getByUserIdAndStatusContainingOrderByStartAt(userId, status, pageRequest);

        LocalDateTime nowTime = LocalDateTime.now();

        List<APIApplicationHistory.Application> responseApplications = new ArrayList<>();

        for (int i = 0; i < applications.getContent().size(); i++) {
            Application application = applications.getContent().get(i);

            Optional<Job> job = jobDao.findById(application.getJobId());

            if (status.equals(ApplicationTypeEnum.COMPLETED.name())) {
                // isReview check 로직

            }

            // 태그
            List<JobTag> tags = jobTagDao.findByJobId(application.getJobId());
            List<JobTagList> jobTags = new ArrayList<>();
            for (JobTag tag : tags) {
                jobTags.add(new JobTagList(tag.getId(), tag.getContents()));
            }

            // giver
            Optional<User> user = userDao.findByIdAndDeleteYn(job.get().getUserId(), "N");
            Giver giver = new Giver(user.get().getId(), user.get().getName(), user.get().getProfileImg());

            //Coordinate
            Coordinate coordinate = new Coordinate(job.get().getLat(), job.get().getLng());

            // jobOverview

            //totalPayment -> COMPLETED일때만 진행.
            int totalPayment = 0;
            if (status.equals(ApplicationTypeEnum.COMPLETED.name())) {
                if (applicationTotalPaymentDao.getByApplicationId(application.getId()) != null)
                    totalPayment = applicationTotalPaymentDao.getByApplicationId(application.getId()).getTotalPayment();
            }
            JobOverview jobOverview = JobOverview.builder().id(job.get().getId()).title(job.get().getTitle()).coordinate(coordinate).giver(giver).startAt(job.get().getStartAt()).endAt(job.get().getEndAt()).pay(job.get().getPay()).payType(job.get().getPayType()).totalPay(totalPayment).tags(jobTags).build();

            //30분 이내라면 true로 변경
            boolean isRequestPossible = false;

            if (nowTime.plusMinutes(30).isAfter(job.get().getStartAt())) isRequestPossible = true;

            APIApplicationHistory.Application responseApplication = new APIApplicationHistory.Application(application.getId(), application.getStatus(), jobOverview, isRequestPossible);
            responseApplications.add(responseApplication);
        }

        Pagination pagination = new Pagination(applications.isLast(), page + 1, applications.getTotalElements());

        APIApplicationHistory.Payload payload = null;
        payload = new APIApplicationHistory.Payload(responseApplications, pagination);

        BasicMeta meta = new BasicMeta(true, "");
        APIApplicationHistory apiApplicationHistory = new APIApplicationHistory(payload, meta);
        response = new ResponseEntity<>(apiApplicationHistory, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateHiredRequest(long id, String accessToken) {

        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        Optional<Application> application = applicationDao.findById(id);
        Optional<Job> job = jobDao.findById(application.get().getJobId());
        LocalDateTime now = LocalDateTime.now();
        long diffInMinutes = ChronoUnit.MINUTES.between(now, job.get().getStartAt());

        //요청 시간이 30분 이내가 아니라면
        if (diffInMinutes > 30) {
            String errorMsg = "일감 시작 요청 시간이 아닙니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        List availableJobStatusList = new ArrayList() {
            {
                add(JobTypeEnum.OPEN.name());
                add(JobTypeEnum.CLOSED.name());
                add(JobTypeEnum.STARTED.name());
            }
        };

        if (availableJobStatusList.contains(job.get().getStatus())) {
            if (!application.get().getStatus().equals(ApplicationTypeEnum.HIRED.name())) {
                String errorMsg = "업무시작요청을 할 수 없습니다.";
                error = new Response(new BasicMeta(false, errorMsg));
                response = new ResponseEntity<>(error, HttpStatus.OK);
                return response;
            }

            LocalDateTime nowTime = LocalDateTime.now(); // 현재 시간을 가져옴

            if (application.get().getStartAt().isBefore(nowTime)) { // 요청시간이 시작시간보다 클 경우
                application.get().setStartAt(nowTime.withSecond(0).withNano(0));// startAt을 현재 시간으로 변경
            }
            application.get().setStatus(ApplicationTypeEnum.HIRED_REQUEST.name());
            applicationDao.save(application.get());

            error = new Response(new BasicMeta(true, "성공적으로 업무 요청하였습니다."));
        } else {
            String errorMsg = "업무가 종료된 공고입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
        }

        response = new ResponseEntity<>(error, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateHiredApproved(long id, String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        Optional<Application> application = applicationDao.findById(id);
        Optional<Job> job = jobDao.findById(application.get().getJobId());

        String jobStatusArr[] = {"OPEN", "STARTED", "CLOSED"};
        List<String> jobStatusList = new ArrayList<>(Arrays.asList(jobStatusArr));
        if (jobStatusList.contains(job.get().getStatus())) {
            if (!application.get().getStatus().equals(ApplicationTypeEnum.HIRED_REQUEST.name())) {
                String errorMsg = "업무 요청부터 해야합니다.";
                error = new Response(new BasicMeta(false, errorMsg));
            } else {
                application.get().setStatus(ApplicationTypeEnum.HIRED_APPROVED.name());
                applicationDao.save(application.get());

                if (job.get().getStatus().equals(JobTypeEnum.CLOSED.name())) {
                    job.get().setStatus(JobTypeEnum.STARTED.name());
                    jobDao.save(job.get());
                }

                String message = "알바가 시작되었습니다.";
                error = new Response(new BasicMeta(true, message));
            }
        } else {
            String errorMsg = "업무가 종료되었습니다.";
            error = new Response(new BasicMeta(false, errorMsg));
        }

        response = new ResponseEntity<>(error, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateHired(List<Long> applicationIds, String accessToken) {

        ResponseEntity response = null;
        Response responseObj = null;

        long jobId = -1;
        for (Long id : applicationIds) {
            Optional<Application> application = applicationDao.findById(id);

            if (application.get().getStatus().equals(ApplicationTypeEnum.APPLIED.name())) {
                application.get().setStatus(ApplicationTypeEnum.HIRED.name());
                applicationDao.save(application.get());
            }

            jobId = application.get().getJobId();
        }

        Optional<Job> job = jobDao.findById(jobId);
        int hiredCount = applicationDao.countByJobIdAndStatusContaining(job.get().getId(), "HIRED");
        // 모집인원이 다 차면 공고 상태 close
        if (job.get().getPersonnel() <= hiredCount && job.get().getStatus().equals(JobTypeEnum.OPEN.name())) {
            job.get().setStatus(JobTypeEnum.CLOSED.name());
            jobDao.save(job.get());
        }

        String message = "채택이 완료되었습니다.";
        responseObj = new Response(new BasicMeta(true, message));
        response = new ResponseEntity<>(responseObj, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getReceiptWorker(long id) {
        ResponseEntity response = null;
        Response error = null;

        Optional<Application> application = applicationDao.findById(id);
        Optional<Job> job = jobDao.findById(application.get().getJobId());

        if (job.isEmpty()) {
            String errorMsg = "존재하지 않는 공고입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        // job tag
        List<APIReceiptWorker.JobTag> responseJobTags = new ArrayList<>();
        List<JobTag> jobTags = jobTagDao.findByJobId(job.get().getId());
        for (JobTag jobTag : jobTags) {
            Optional<APIReceiptWorker.JobTag> responseJobTag = Optional.of(new APIReceiptWorker.JobTag(jobTag.getContents()));

            responseJobTags.add(responseJobTag.get());
        }

        // user
        Optional<User> user = userDao.findByIdAndDeleteYn(job.get().getUserId(), "N");
        Optional<APIReceiptWorker.User> responseUser = Optional.of(new APIReceiptWorker.User(user.get().getId(), user.get().getProfileImg()));

        //totalPayment
        int totalPayment = applicationTotalPaymentDao.getByApplicationId(id).getTotalPayment();
        Optional<APIReceiptWorker.Job> responseJob = Optional.of(new APIReceiptWorker.Job(job.get().getId(), job.get().getTitle(), job.get().getStartAt(), job.get().getEndAt(), totalPayment, responseJobTags, responseUser.get()));

        // TODO: isReview 체크
        Optional<APIReceiptWorker.Application> responseApplication = Optional.of(new APIReceiptWorker.Application(application.get().getId(), true, responseJob.get()));

        APIReceiptWorker.Payload payload = new APIReceiptWorker().new Payload(responseApplication.get());
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APIReceiptWorker(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateAppliedCancel(long id) {

        ResponseEntity response = null;
        Response responseObj = null;

        Optional<Application> application = applicationDao.findById(id);
        Optional<Job> job = jobDao.findById(application.get().getJobId());

        if (!application.get().getStatus().equals(ApplicationTypeEnum.APPLIED.name())) {
            String errorMsg = "정보가 변경되었습니다.";
            responseObj = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(responseObj, HttpStatus.OK);
            return response;
        }

        application.get().setStatus(ApplicationTypeEnum.CANCELED.name());
        applicationDao.save(application.get());

        // 채택된 인원이 모집 인원보다 적을때 job 다시 open (채택 후 취소만 해당 - 구현 아직 안됨)
        int hiredCount = applicationDao.countByJobIdAndStatus(job.get().getId(), ApplicationTypeEnum.HIRED.name());
        if (hiredCount < job.get().getPersonnel()) {
            if (job.get().getStatus().equals(JobTypeEnum.CLOSED.name())) {
                job.get().setStatus(JobTypeEnum.OPEN.name());
            }
        }

        String message = "지원 취소가 완료되었습니다.";
        responseObj = new Response(new BasicMeta(true, message));
        response = new ResponseEntity<>(responseObj, HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateRejected(List<Long> applicationIds) {
        ResponseEntity response = null;
        BasicMeta meta = null;
        List<Long> failedList = new ArrayList<>();
        long jobId = 0;

        for (Long id : applicationIds) {
            Optional<Application> application = applicationDao.findById(id);
            jobId = application.orElseThrow().getJobId();
            application.ifPresentOrElse(rejectedApplication -> {
                        if (rejectedApplication.getStatus().equals(ApplicationTypeEnum.HIRED_APPROVED.name())) {
                            failedList.add(id);
                            return;
                        }
                        rejectedApplication.setStatus(ApplicationTypeEnum.REJECTED.name());
                        applicationDao.save(rejectedApplication);
                    },
                    () -> {
                        failedList.add(id);
                    });
        }
        //job에 %hired%(채택된) 지원서가 job의 personnel보다 작으면 job status 를 OPEN으로 변경
        List<Application> applicationCheckList = applicationDao.findByJobIdAndStatusContaining(jobId, "HIRED");

        jobDao.findById(jobId).ifPresent(checkJob -> {
            if (checkJob.getPersonnel() > applicationCheckList.size()) {
                checkJob.setStatus(JobTypeEnum.OPEN.name());
                jobDao.save(checkJob);
            }
        });

        //failedList 보여줄지말지.

        meta = new BasicMeta(true, "변경되었습니다.");
        SuccessResponse result = new SuccessResponse(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity summaryApplication(String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        int appliedCount = applicationDao.countByUserIdAndStatus(userId, ApplicationTypeEnum.APPLIED.name());

        List<String> hiredList = new ArrayList<>();
        hiredList.add(ApplicationTypeEnum.HIRED.name());
        hiredList.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        hiredList.add(ApplicationTypeEnum.HIRED_APPROVED.name());

        int hiredCount = applicationDao.countByUserIdAndStatusIn(userId, hiredList);

        APIApplicationStatusOverview.StatusOverview statusOverview = new APIApplicationStatusOverview.StatusOverview(appliedCount, hiredCount);
        APIApplicationStatusOverview.Payload payload = new APIApplicationStatusOverview.Payload(statusOverview);
        APIApplicationStatusOverview apiApplicationStatusOverview = new APIApplicationStatusOverview(payload, new BasicMeta(true, ""));
        response = new ResponseEntity<>(apiApplicationStatusOverview, HttpStatus.OK);
        return response;
    }
}

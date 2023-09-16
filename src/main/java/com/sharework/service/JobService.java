package com.sharework.service;

import com.sharework.common.ApplicationTypeEnum;
import com.sharework.common.JobTypeEnum;
import com.sharework.dao.*;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.model.model.BaseBenefit;
import com.sharework.request.model.AppliedList;
import com.sharework.request.model.JobDetail;
import com.sharework.request.model.JobLocation;
import com.sharework.request.model.RegisterJob;
import com.sharework.response.model.*;
import com.sharework.response.model.job.*;
import com.sharework.response.model.job.APICompletedList.CompletedJob;
import com.sharework.response.model.job.APICompletedList.JobCompletedPayload;
import com.sharework.response.model.job.APIPreviousJobs.JobPreviousPayload;
import com.sharework.response.model.job.APIProceedingList.Groupstatus;
import com.sharework.response.model.job.APIProceedingList.JobProceedingPayload;
import com.sharework.response.model.job.APIProceedingList.ProceedingJob;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.user.Giver;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    @Autowired
    JobDao jobDao;
    @Autowired
    ApplicationDao applicationDao;
    @Autowired
    UserChecklistDao userChecklistDao;
    @Autowired
    TokenIdentification identification;
    @Autowired
    TagService tagService;
    @Autowired
    ChecklistService userChecklistService;
    @Autowired
    UserDao userDao;
    @Autowired
    JobBenefitDao jobBenefitDao;
    @Autowired
    JobTagDao jobTagDao;
    @Autowired
    JobCheckListDao jobCheckListDao;
    @Autowired
    ApplicationChecklistDao applicationChecklistDao;
    @Autowired
    BaseBenefitDao baseBenefitDao;

    private final ApplicationTotalPaymentDao applicationTotalPaymentDao;
    private final int PAGE_SIZE = 5;

    public ResponseEntity getJobList(JobLocation getJob) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        List<Job> jobList = jobDao.selectJobs(getJob.getSouthwestLat(), getJob.getNortheastLat(),
                getJob.getSouthwestLng(), getJob.getNortheastLng());
        BasicMeta meta;
        if (jobList.isEmpty()) {
            String errorMsg = "등록된 일감이 없습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        List<MainJobsResponse> mainJobsResponse = new ArrayList<MainJobsResponse>();

        for (Job job : jobList)
            mainJobsResponse.add(
                    new MainJobsResponse(job.getId(), new Coordinate(job.getLat(), job.getLng())));

        JobsPayload jobPayload = new JobsPayload(mainJobsResponse);
        meta = new BasicMeta(true, "공고 목록을 성공적으로 전달하였습니다.");
        final JobsResponse result = new JobsResponse(jobPayload, meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @Transactional
    public ResponseEntity insertJob(String accessToken, RegisterJob registerJob) {
        ResponseEntity response = null;
        ErrorResponse error = null;
        BasicMeta meta;
        long id = identification.getHeadertoken(accessToken);

        // 시작 끝 날짜 localtime으로 변경 후 저장(끝나는 시간이 시작보다 작을 시 끝나는 시간은 다음날로 변경)
        String startAt = registerJob.getDateAt() + " " + registerJob.getStartAt();
        String endAt = registerJob.getDateAt() + " " + registerJob.getEndAt();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startAt, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endAt, formatter);


        if (startTime.isAfter(endTime)) {
            endTime = endTime.plusDays(1);
        }
        // job저장
        Job job = jobDao.save(Job.builder().userId(id).title(registerJob.getTitle()).startAt(startTime).endAt(endTime)
                .lat(registerJob.getLat()).lng(registerJob.getLng()).address(registerJob.getAddress())
                .addressDetail(registerJob.getAddressDetail()).personnel(registerJob.getPersonnel())
                .payType(registerJob.getPayType()).pay(registerJob.getPay()).contents(registerJob.getContents())
                .status(registerJob.getStatus()).paymentToday(registerJob.getPaymentToday()).build());

        // tag 저장
        long jobId = job.getId();
        if (!tagService.insertJobTag(registerJob.getTagSubList(), jobId)) {
            String errorMsg = "잘못된 태그입니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        // checkList 저장
        userChecklistService.insertJobCheckList(registerJob.getCheckList(), jobId);

        meta = new BasicMeta(true, "공고가 성공적으로 저장되었습니다.");
        SuccessResponse result = new SuccessResponse(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity jobClusterDetail(JobDetail jobDetail) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        PageRequest pageRequest = PageRequest.of(jobDetail.getPage(), jobDetail.getPageSize());
        Page<Job> job = jobDao.findJobsDetail(jobDetail.getJobIds(), pageRequest);


        List<JobOverview> jobOverviewList = new ArrayList<JobOverview>();

        for (int i = 0; i < job.getContent().size(); i++) {
            Job getJob = job.getContent().get(i);
            JobOverview jobOverview = new JobOverview();
            List<JobTag> jobTag = jobTagDao.findByJobId(getJob.getId());
            User user = userDao.findByIdAndDeleteYn(getJob.getUserId(), "N").orElseThrow();

            List<JobTagList> tags = new ArrayList<JobTagList>();
            for (JobTag tag : jobTag) {
                String contents = tag.getContents();
                tags.add(new JobTagList(tag.getId(), tag.getContents()));
            }
            jobOverviewList.add(JobOverview.builder().
                    id(getJob.getId()).title(getJob.getTitle()).
                    coordinate(new Coordinate(getJob.getLat(), getJob.getLng())).
                    startAt(getJob.getStartAt()).endAt(getJob.getEndAt()).pay(getJob.getPay()).payType(getJob.getPayType()).
                    giver(new Giver(user.getId(), user.getName(), user.getProfileImg())).tags(tags).totalPay(0)
                    .build());
        }

        APIJobClusterDetail.Pagination pagination = new APIJobClusterDetail.Pagination(job.isLast(), job.getTotalPages(), jobDetail.getPage());
        APIJobClusterDetail.Payload payload = new APIJobClusterDetail.Payload(jobOverviewList, pagination);
        APIJobClusterDetail apiJobClusterDetail = new APIJobClusterDetail(payload, new BasicMeta(true, ""));
        response = new ResponseEntity<>(apiJobClusterDetail, HttpStatus.OK);
        return response;
    }

    public ResponseEntity jobDetail(long id, String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);
        String userType = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow().getUserType();

        Optional<Job> job = jobDao.findById(id);

        if (job.isEmpty()) {
            String errorMsg = "등록된 일감이 없습니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        //회원이 지원한 공고라면 정보 제공.
        List<Application> applicationCheck = applicationDao.findByJobIdAndUserIdAndStatusContaining(job.get().getId(), userId, "HIRED");

        if (userType.equals("worker") && (applicationCheck.isEmpty() && !job.get().getStatus().equals(JobTypeEnum.OPEN.name()))) {
            String errorMsg = "마감된 공고 입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        // giver user
        Optional<User> user = userDao.findByIdAndDeleteYn(job.get().getUserId(), "N");
        Optional<APIJobDetail.User> responseUser = Optional.of(new APIJobDetail.User(user.get().getId(), user.get().getName(), user.get().getProfileImg()));

        // 제공사항
        List<JobBenefit> benefits = jobBenefitDao.findByJobId(id);
        List<APIJobDetail.JobBenefit> responseBenefits = new ArrayList<>();
        for (JobBenefit benefit : benefits) {
//            benefit.getBaseBenefitId()
            Optional<BaseBenefit> baseJobBenefit = baseBenefitDao.findById(benefit.getBaseBenefitId());
            responseBenefits.add(new APIJobDetail.JobBenefit(baseJobBenefit.get().getContents()));
        }

        // 태그
        List<JobTag> tags = jobTagDao.findByJobId(id);
        List<APIJobDetail.JobTag> responseTags = new ArrayList<>();
        for (JobTag tag : tags) {
            responseTags.add(new APIJobDetail.JobTag(tag.getId(), tag.getContents()));
        }

        // 체크리스트
        List<JobCheckList> checkLists = jobCheckListDao.findByJobId(id);
        List<APIJobDetail.JobChecklist> responseChecklists = new ArrayList<>();
        for (JobCheckList checkList : checkLists)
            responseChecklists.add(new APIJobDetail.JobChecklist(checkList.getCheckListId(), checkList.getContents()));


        //worker라면 addressDetail이 HIRED...,COMPLETED 일 경우에만 나와야한다.
        List<String> accessAddressDetailStatus = new ArrayList<>();
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.COMPLETED.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.COMPLETED_REVIEWED.name());

        boolean accessAddressDetail = true;
        String jobStatus = applicationDao.findByJobIdAndUserId(id, userId) == null ? "FAILED" : applicationDao.findByJobIdAndUserId(id, userId).getStatus();

        if (userType.equals("worker") && !accessAddressDetailStatus.contains(jobStatus))
            accessAddressDetail = false;

        APIJobDetail.Coordinate coordinate = new APIJobDetail.Coordinate(job.get().getLat(), job.get().getLng());
        Optional<APIJobDetail.Job> responseJob = Optional.of(new APIJobDetail.Job(job.get().getId(), job.get().getTitle(), job.get().getStartAt(),
                job.get().getEndAt(), coordinate, job.get().getAddress(), accessAddressDetail ? job.get().getAddressDetail() : null,
                job.get().getPersonnel(), job.get().getPayType(), job.get().getPay(), job.get().getContents(), job.get().getStatus(),
                responseUser.get(), responseBenefits, responseTags, responseChecklists));

        //본인이 지원했는지
        Application application = applicationDao.findByJobIdAndUserId(job.get().getId(), userId);
        boolean didApply = !Objects.isNull(application);

        APIJobDetail.Payload payload = new APIJobDetail.Payload(responseJob.get(), didApply);
        APIJobDetail apiJobDetail = new APIJobDetail(payload, new BasicMeta(true, ""));

        response = new ResponseEntity<>(apiJobDetail, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getHiredList(long id, String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(ApplicationTypeEnum.HIRED.name());
        status.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        status.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        status.add(ApplicationTypeEnum.COMPLETED.name());

        List<Application> applications = applicationDao.findByJobIdAndStatusIn(id, status);

        List<APIHiredList.Application> responseApplications = new ArrayList<>();
        List<JobCheckList> jobCheckLists = jobCheckListDao.findByJobId(id);
        for (Application application : applications) {

            // user
            Optional<User> user = userDao.findByIdAndDeleteYn(application.getUserId(), "N");

            int experienceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            Optional<APIHiredList.Worker> responseUser = Optional.of(new APIHiredList.Worker(new APIHiredList.User(user.get().getId(), user.get().getName(),
                    user.get().getProfileImg()), experienceCount, absenceCount));

            Boolean isAction = false;
            if (application.getStatus().equals(ApplicationTypeEnum.HIRED.name())) {
                isAction = true;
            }

            // 지원서 체크리스트
            List<ApplicationChecklist> applicationChecklists = applicationChecklistDao.findByApplicationId(application.getId());
            List<APIHiredList.ApplicationChecklist> responseApplicationChecklists = new ArrayList<>();
            for (JobCheckList jobCheckList : jobCheckLists) {
                boolean isChecked = false;
                long useJobCheckList = applicationChecklists.stream().filter(applicationChecklist -> applicationChecklist.getJobChecklistId() == jobCheckList.getCheckListId()).count();
                if (useJobCheckList > 0)
                    isChecked = true;
//                boolean isResponse = false;
//                for (ApplicationChecklist applicationChecklist : applicationChecklists) {
//                    if (jobCheckListDao.findById(applicationChecklist.getJobChecklistId()).isPresent()) {
//                        isResponse = true;
//                        break;
//                    }
//                }

//                Optional<UserChecklist> userChecklist = userChecklistDao.findByid(jobCheckList.getCheckListId());
                responseApplicationChecklists.add(new APIHiredList.ApplicationChecklist(jobCheckList.getContents(), isChecked));
            }

            Optional<APIHiredList.Application> responseApplication = Optional.of(new APIHiredList.Application(
                    application.getId(), application.getStatus(), responseUser.get(), responseApplicationChecklists));

            responseApplications.add(responseApplication.get());
        }
        Pagination pagination = new Pagination(true, 1, 20);     //TODO: 정상로직으로 수정필요 빌드만 되도록 수정해 놓음
        APIHiredList.Payload payload = new APIHiredList().new Payload(responseApplications, pagination);
        APIHiredList.Meta meta = new APIHiredList.Meta(true, "", applications.size());

        response = new ResponseEntity<>(new APIHiredList(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getAppliedList(long id, AppliedList appliedList, String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        PageRequest pageRequest = PageRequest.of(appliedList.getPage(), appliedList.getPageSize());
        Page<Application> applications = applicationDao.findByJobIdAndApplied(id, ApplicationTypeEnum.APPLIED.name(), pageRequest);


        List<APIAppliedList.Application> responseApplications = new ArrayList<>();
        List<JobCheckList> jobCheckLists = jobCheckListDao.findByJobId(id);

        for (Application application : applications) {
            // user
            Optional<User> user = userDao.findByIdAndDeleteYn(application.getUserId(), "N");

            int experienceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            Optional<APIAppliedList.Worker> responseUser = Optional.of(new APIAppliedList.Worker(new APIAppliedList.User(user.get().getId(), user.get().getName(),
                    user.get().getProfileImg()), experienceCount, absenceCount));

            // 지원서 체크리스트
            List<ApplicationChecklist> applicationChecklists = applicationChecklistDao.findByApplicationId(application.getId());
            List<APIAppliedList.ApplicationChecklist> responseApplicationChecklists = new ArrayList<>();

            for (JobCheckList jobCheckList : jobCheckLists) {
                boolean isChecked = false;
                long useJobCheckList = applicationChecklists.stream().filter(applicationChecklist -> applicationChecklist.getJobChecklistId() == jobCheckList.getCheckListId()).count();
                if (useJobCheckList > 0)
                    isChecked = true;
//                for (ApplicationChecklist applicationChecklist : applicationChecklists) {
//                    if (jobCheckListDao.findById(applicationChecklist.getJobChecklistId()).isPresent()) {
//                        isResponse = true;
//                        break;
//                    }
//                    userReview.ifPresent(selectUserReview -> {
//                        selectUserReview.setCount(selectUserReview.getCount() + 1);
//                        userReviewDao.save(selectUserReview);
//                    });
//                }

//                Optional<UserChecklist> userChecklist = userChecklistDao.findByid(jobCheckList.getCheckListId());
                responseApplicationChecklists.add(new APIAppliedList.ApplicationChecklist(jobCheckList.getContents(), isChecked));
            }

            Optional<APIAppliedList.Application> responseApplication = Optional.of(new APIAppliedList.Application(
                    application.getId(), application.getStatus(), responseUser.get(), responseApplicationChecklists));

            responseApplications.add(responseApplication.get());
        }
//        new APIJobClusterDetail.Pagination(job.isLast(), job.getTotalPages(), jobDetail.getPage());

        APIAppliedList.Pagination pagination = new APIAppliedList.Pagination(applications.isLast(), applications.getTotalPages(), appliedList.getPage());
        APIAppliedList.Payload payload = new APIAppliedList().new Payload(responseApplications, pagination);
        APIAppliedList.Meta meta = new APIAppliedList.Meta(true, "");

        response = new ResponseEntity<>(new APIAppliedList(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getReceiptGiver(long id) {
        ResponseEntity response = null;
        Response error = null;

        Optional<Job> job = jobDao.findById(id);

        if (job.isEmpty()) {
            String errorMsg = "존재하지 않는 공고입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        List<APIReceiptGiver.ApplicationOverview> responseApplications = new ArrayList<>();
        List<Application> applications = applicationDao.findByJobIdAndStatus(id, ApplicationTypeEnum.COMPLETED.name()); // 해당 공고에 일이 완료된 지원서 가져오기

        for (Application application : applications) {
            // user
            Optional<User> user = userDao.findByIdAndDeleteYn(application.getUserId(), "N");
            Optional<APIReceiptGiver.User> responseUser = Optional.of(new APIReceiptGiver.User(user.get().getId(), user.get().getName(),
                    user.get().getProfileImg()));

            // TODO:해당 지원서에 리뷰 작성했는지 체크


            int experienceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            Optional<APIReceiptGiver.Worker> responseWorker = Optional.of(new APIReceiptGiver.Worker(
                    responseUser.get(), experienceCount, absenceCount));

            // payment 계산
//            int payment = 0;
            int payment = applicationTotalPaymentDao.getByApplicationId(application.getId()).getTotalPayment();

            responseApplications.add(new APIReceiptGiver.ApplicationOverview(application.getId(), responseWorker.get(), application.getStartAt(), application.getEndAt(), payment, application.getStatus()));
        }

        APIReceiptGiver.Payload payload = new APIReceiptGiver().new Payload(responseApplications);
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APIReceiptGiver(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getPreviousJobs(String accessToken) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        List<APIPreviousJobs.Job> responseJobs = new ArrayList<>();
        List<Job> jobs = jobDao.findTop10ByUserIdOrderByIdDesc(userId); // 상위 10개 공고

        for (Job job : jobs) {
            List<JobCheckList> checklist = jobCheckListDao.findByJobId(job.getId());
            // List<String> checklistContents = checklist.stream().map(JobCheckList::getContents).collect(Collectors.toList());
            List<String> checklistContents = new ArrayList<>();
            for (JobCheckList jobCheckList : checklist) {
                checklistContents.add(jobCheckList.getContents());
            }

            Optional<APIPreviousJobs.Job> responseJob = Optional.of(new APIPreviousJobs.Job(
                    job.getTitle(), job.getStartAt(), job.getEndAt(), job.getPayType(), job.getPay(), job.getContents(),
                    job.getCreatedAt(), job.getLat(), job.getLng(), job.getAddressDetail(), checklistContents));

            responseJobs.add(responseJob.get());
        }

        JobPreviousPayload payload = new JobPreviousPayload(responseJobs);
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APIPreviousJobs(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getProceedingList(String accessToken, Integer page) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(JobTypeEnum.OPEN.name());
        status.add(JobTypeEnum.STARTED.name());
        status.add(JobTypeEnum.CLOSED.name());

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Job> jobs = jobDao.findByUserIdAndStatusIn(userId, status, pageRequest);
        List<ProceedingJob> responseJobs = new ArrayList<>();

        for (Job job : jobs) {
            // status 별로 계산
            String applicationStatus = "";
            int applicationCount = 0;
            if (job.getStatus().equals(JobTypeEnum.OPEN.name())) {
                applicationStatus = ApplicationTypeEnum.APPLIED.name();
                applicationCount = applicationDao.countByJobIdAndStatus(job.getId(), applicationStatus);
            } else if (job.getStatus().equals(JobTypeEnum.CLOSED.name())) {
                applicationStatus = ApplicationTypeEnum.HIRED.name();
                applicationCount = applicationDao.countByJobIdAndStatus(job.getId(), applicationStatus);
            } else if (job.getStatus().equals(JobTypeEnum.STARTED.name())) {
                applicationStatus = ApplicationTypeEnum.HIRED_APPROVED.name();
                applicationCount = applicationDao.countByJobIdAndStatus(job.getId(), applicationStatus);
            }

            // payment 계산
            int payment = 0;
            List<ApplicationTotalPayment> applicationTotalPaymentList = applicationTotalPaymentDao.getByJobId(job.getId());

            for (ApplicationTotalPayment app : applicationTotalPaymentList)
                payment += app.getTotalPayment();

            //tags
            List<JobTag> tags = jobTagDao.findByJobId(job.getId());
            Groupstatus groupstatus = new Groupstatus(applicationStatus, applicationCount);

            Optional<ProceedingJob> responseJob = Optional.of(new ProceedingJob(
                    job.getId(), job.getTitle(), job.getStartAt(), job.getEndAt(), applicationCount, groupstatus, job.getStatus(), payment, tags));

            responseJobs.add(responseJob.get());
        }

        Pagination pagination = new Pagination(jobs.isLast(), page, jobs.getTotalPages());
        JobProceedingPayload jobProceedingPayload = new JobProceedingPayload(responseJobs, pagination);
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APIProceedingList(jobProceedingPayload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getCompletedList(String accessToken, Integer page) {
        ResponseEntity response = null;
        Response error = null;

        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(JobTypeEnum.COMPLETED.name());

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Job> jobs = jobDao.findByUserIdAndStatusIn(userId, status, pageRequest);
        List<CompletedJob> responseJobs = new ArrayList<>();

        for (Job job : jobs) {
            String applicationStatus = "";
            int applicationCount = 0;
            if (job.getStatus().equals(JobTypeEnum.COMPLETED.name())) {
                applicationStatus = ApplicationTypeEnum.COMPLETED.name();
                applicationCount = applicationDao.countByJobIdAndStatus(job.getId(), applicationStatus);
            }


            // payment 계산
            int payment = 0;
            List<ApplicationTotalPayment> applicationTotalPaymentList = applicationTotalPaymentDao.getByJobId(job.getId());

            for (ApplicationTotalPayment app : applicationTotalPaymentList)
                payment += app.getTotalPayment();


            //tags
            List<JobTag> tags = jobTagDao.findByJobId(job.getId());
            Groupstatus groupstatus = new Groupstatus(applicationStatus, applicationCount);

            Optional<CompletedJob> responseJob = Optional.of(new CompletedJob(
                    job.getId(), job.getTitle(), job.getStartAt(), job.getEndAt(), applicationCount, groupstatus, job.getStatus(), payment, tags));

            responseJobs.add(responseJob.get());
        }

        Pagination pagination = new Pagination(jobs.isLast(), page, jobs.getTotalPages());
        JobCompletedPayload payload = new JobCompletedPayload(responseJobs, pagination);
        BasicMeta meta = new BasicMeta(true, "");

        response = new ResponseEntity<>(new APICompletedList(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity updateStatusClosed(long id) {
        ResponseEntity response = null;

        final BasicMeta finalmeta = new BasicMeta();
        jobDao.findById(id).ifPresentOrElse(job -> {

            // 공고 채택 마감 시 채택 인원이 0이면 failed로 변경
            if (applicationDao.findByJobIdAndStatus(id, ApplicationTypeEnum.HIRED.name()).size() > 0)
                job.setStatus(JobTypeEnum.CLOSED.name());
            else
                job.setStatus(JobTypeEnum.FAILED.name());
            jobDao.save(job);
            finalmeta.setStatus(true);
            finalmeta.setMessage("성공적으로 변경하였습니다.");
        }, () -> {
            String errorMsg = "공고가존재하지않습니다.";
            finalmeta.setStatus(false);
            finalmeta.setMessage(errorMsg);
        });

        SuccessResponse result = new SuccessResponse(finalmeta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getMinimumWage() {
        ResponseEntity response = null;

        GetMinimumWagePayload payload = new GetMinimumWagePayload(9620);
        final BasicMeta meta = new BasicMeta(true, "");
        response = new ResponseEntity<>(new GetMinimumWageResponse(payload, meta), HttpStatus.OK);
        return response;
    }

    public ResponseEntity getJobHiredInfo(long jobId, long applicationId) {
        ResponseEntity response = null;
        Response error = null;

        Optional<Job> jobOptional = jobDao.findById(jobId);

        if (jobOptional.isEmpty()) {
            String errorMsg = "존재하지 않는 공고입니다.";
            error = new Response(new BasicMeta(false, errorMsg));
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        Job job = jobOptional.get();
        List<JobTag> jobTagList = jobTagDao.findByJobId(job.getId());
        long workerId = applicationDao.findById(applicationId).get().getUserId();
        User worker = userDao.findById(workerId).get();
        User giver = userDao.findById(job.getUserId()).get();

        JobHiredInfo jobHiredInfo = JobHiredInfo.builder()
                .payload(JobHiredInfoPayload.of(job, jobTagList, worker, giver))
                .meta(new BasicMeta(true, ""))
                .build();
        response = new ResponseEntity<>(jobHiredInfo, HttpStatus.OK);
        return response;

    }
}

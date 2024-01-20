package com.sharework.service;

import com.sharework.common.ApplicationTypeEnum;
import com.sharework.common.JobTypeEnum;
import com.sharework.dao.*;
import com.sharework.global.NotFoundException;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.*;
import com.sharework.model.model.BaseBenefit;
import com.sharework.request.model.AppliedList;
import com.sharework.request.model.JobDetail;
import com.sharework.request.model.JobLocation;
import com.sharework.request.model.RegisterJob;
import com.sharework.response.model.*;
import com.sharework.response.model.job.*;
import com.sharework.response.model.job.CompletedListResponse.CompletedJob;
import com.sharework.response.model.job.CompletedListResponse.JobCompletedPayload;
import com.sharework.response.model.job.AppliedListResponse.AppliedListApplication;
import com.sharework.response.model.job.AppliedListResponse.AppliedListApplicationChecklist;
import com.sharework.response.model.job.AppliedListResponse.AppliedListMeta;
import com.sharework.response.model.job.AppliedListResponse.AppliedListPagination;
import com.sharework.response.model.job.AppliedListResponse.AppliedListPayload;
import com.sharework.response.model.job.AppliedListResponse.AppliedListUser;
import com.sharework.response.model.job.AppliedListResponse.AppliedListWorker;
import com.sharework.response.model.job.HiredListResponse.HiredListApplication;
import com.sharework.response.model.job.HiredListResponse.HiredListApplicationChecklist;
import com.sharework.response.model.job.HiredListResponse.HiredListMeta;
import com.sharework.response.model.job.HiredListResponse.HiredListPayload;
import com.sharework.response.model.job.JobSimpleResponse.JobSimple;
import com.sharework.response.model.job.JobSimpleResponse.JobSimplePayload;
import com.sharework.response.model.job.PreviousJobResponse.PreviousJob;
import com.sharework.response.model.job.PreviousJobResponse.JobPreviousPayload;
import com.sharework.response.model.job.ProceedingListResponse.JobProceedingPayload;
import com.sharework.response.model.job.ProceedingListResponse.ProceedingJob;
import com.sharework.response.model.job.ProceedingListResponse.ProceedingGroupStatus;
import com.sharework.response.model.job.HiredListResponse.HiredListUser;
import com.sharework.response.model.job.HiredListResponse.HiredListWorker;
import com.sharework.response.model.job.JobClusterDetailResponse.JobClusterDetailPagination;
import com.sharework.response.model.job.JobClusterDetailResponse.JobClusterDetailPayload;
import com.sharework.response.model.job.JobDetailResponse.JobDetailCoordinate;
import com.sharework.response.model.job.JobDetailResponse.JobDetailJob;
import com.sharework.response.model.job.JobDetailResponse.JobDetailJobBenefit;
import com.sharework.response.model.job.JobDetailResponse.JobDetailJobChecklist;
import com.sharework.response.model.job.JobDetailResponse.JobDetailJobTag;
import com.sharework.response.model.job.JobDetailResponse.JobDetailPayload;
import com.sharework.response.model.job.JobDetailResponse.JobDetailUser;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.user.Giver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.sharework.response.model.job.CompletedListResponse.CompletedGroupStatus;
import com.sharework.response.model.job.ReceiptGiverResponse.RgApplicationOverview;
import com.sharework.response.model.job.ReceiptGiverResponse.RgUser;
import com.sharework.response.model.job.ReceiptGiverResponse.RgWorker;
import com.sharework.response.model.job.ReceiptGiverResponse.RgPayload;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobDao jobDao;
    private final ApplicationDao applicationDao;
    private final UserChecklistDao userChecklistDao;
    private final TokenIdentification identification;
    private final TagService tagService;
    private final ChecklistService userChecklistService;
    private final UserDao userDao;
    private final JobBenefitDao jobBenefitDao;
    private final JobTagDao jobTagDao;
    private final JobCheckListDao jobCheckListDao;
    private final ApplicationChecklistDao applicationChecklistDao;
    private final BaseBenefitDao baseBenefitDao;
    private final ApplicationTotalPaymentDao applicationTotalPaymentDao;
    private final ReviewDao reviewDao;
    private final int PAGE_SIZE = 5;

    public JobSimpleResponse getJobSimple(long id) {
        Optional<Job> job = jobDao.findById(id);

        if (job.isEmpty()) {
            throw new NotFoundException("등록된 일감이 없습니다.");
        }

        // 태그
        List<JobTag> tags = jobTagDao.findByJobId(id);
        List<JobDetailJobTag> responseTags = new ArrayList<>();
        for (JobTag tag : tags) {
            responseTags.add(new JobDetailJobTag(tag.getId(), tag.getContents()));
        }

        int payment = 0;
        List<ApplicationTotalPayment> applicationTotalPaymentList = applicationTotalPaymentDao.getByJobId(job.get().getId());

        for (ApplicationTotalPayment app : applicationTotalPaymentList) {
            payment += app.getTotalPayment();
        }

        List<String> proceedings = Arrays.asList(
                JobTypeEnum.OPEN.name(),
                JobTypeEnum.CLOSED.name(),
                JobTypeEnum.STARTED.name()
        );
        com.sharework.response.model.application.GroupStatus status = new com.sharework.response.model.application.GroupStatus();  // FIXME: refactor with GroupStatus I/F.
        if (Objects.equals(job.get().getStatus(), JobTypeEnum.COMPLETED.name())) {
            GroupStatus groupStatus = applicationDao.completedGroupStatus(job.get().getId());

            if (groupStatus != null) {
                status.setName(groupStatus.getName());
                status.setCount(groupStatus.getCount());
            } else {
                status.setName(ApplicationTypeEnum.COMPLETED.name());
                status.setCount(0);
            }
        } else if (proceedings.contains(job.get().getStatus())) {
            GroupStatus groupStatus = applicationDao.processingGroupStatus(job.get().getId());

            if (groupStatus != null) {
                status.setName(groupStatus.getName());
                status.setCount(groupStatus.getCount());
            } else {
                status.setName(ApplicationTypeEnum.APPLIED.name());
                status.setCount(0);
            }
        } else {
            status.setName("");
            status.setCount(0);
        }

        Optional<JobSimple> responseJob = Optional.of(new JobSimple(
                job.get().getEndAt(),
                job.get().getId(),
                status,
                payment,
                job.get().getStartAt(),
                job.get().getStatus(),
                responseTags,
                job.get().getTitle())
        );

        JobSimplePayload payload = new JobSimplePayload(responseJob.get());
        return new JobSimpleResponse(payload, new BasicMeta(true, ""));
    }

    public JobResponse getJobList(JobLocation getJob) {
        List<Job> jobList = jobDao.selectJobs(getJob.getSouthwestLat(), getJob.getNortheastLat(),
                getJob.getSouthwestLng(), getJob.getNortheastLng());
        BasicMeta meta;
        if (jobList.isEmpty()) {
            throw new NotFoundException("등록된 일감이 없습니다.");
        }

        List<MainJobsResponse> mainJobsResponse = new ArrayList<MainJobsResponse>();

        for (Job job : jobList)
            mainJobsResponse.add(
                    new MainJobsResponse(job.getId(), new Coordinate(job.getLat(), job.getLng())));

        JobsPayload jobPayload = new JobsPayload(mainJobsResponse);
        meta = new BasicMeta(true, "공고 목록을 성공적으로 전달하였습니다.");
        return new JobResponse(jobPayload, meta);
    }

    @Transactional
    public SuccessResponse insertJob(String accessToken, RegisterJob registerJob) {
        long id = identification.getHeadertoken(accessToken);

        // 시작 끝 날짜 localtime으로 변경 후 저장(끝나는 시간이 시작보다 작을 시 끝나는 시간은 다음날로 변경)
        String startAt = registerJob.getStartAt();
        String endAt = registerJob.getEndAt();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
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
            return new SuccessResponse(new BasicMeta(false, "잘못된 태그입니다."));
        }

        // checkList 저장
        userChecklistService.insertJobCheckList(registerJob.getCheckList(), jobId);

        return new SuccessResponse(new BasicMeta(true, "공고가 성공적으로 저장되었습니다."));
    }

    public JobClusterDetailResponse jobClusterDetail(JobDetail jobDetail) {
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

        JobClusterDetailPagination pagination = new JobClusterDetailPagination(job.isLast(), job.getTotalPages(), jobDetail.getPage());
        JobClusterDetailPayload payload = new JobClusterDetailPayload(jobOverviewList, pagination);
        return new JobClusterDetailResponse(payload, new BasicMeta(true, ""));
    }

    public JobDetailResponse jobDetail(long id, String accessToken) {
        long userId = identification.getHeadertoken(accessToken);
        String userType = userDao.findByIdAndDeleteYn(userId, "N").orElseThrow().getUserType();

        Optional<Job> job = jobDao.findById(id);

        if (job.isEmpty()) {
            throw new NotFoundException("등록된 일감이 없습니다.");
        }

        //회원이 지원한 공고라면 정보 제공.
        List<Application> applicationCheck = applicationDao.findByJobIdAndUserIdAndStatusContaining(job.get().getId(), userId, "HIRED");

        if (userType.equals("worker") && (applicationCheck.isEmpty() && !job.get().getStatus().equals(JobTypeEnum.OPEN.name()))) {
            throw new NotFoundException("마감된 공고 입니다.");
        }

        // giver user
        Optional<User> user = userDao.findByIdAndDeleteYn(job.get().getUserId(), "N");
        Optional<JobDetailUser> responseUser = Optional.of(new JobDetailUser(user.get().getId(), user.get().getName(), user.get().getProfileImg()));

        // 제공사항
        List<JobBenefit> benefits = jobBenefitDao.findByJobId(id);
        List<JobDetailJobBenefit> responseBenefits = new ArrayList<>();
        for (JobBenefit benefit : benefits) {
//            benefit.getBaseBenefitId()
            Optional<BaseBenefit> baseJobBenefit = baseBenefitDao.findById(benefit.getBaseBenefitId());
            responseBenefits.add(new JobDetailJobBenefit(baseJobBenefit.get().getContents()));
        }

        // 태그
        List<JobTag> tags = jobTagDao.findByJobId(id);
        List<JobDetailJobTag> responseTags = new ArrayList<>();
        for (JobTag tag : tags) {
            responseTags.add(new JobDetailJobTag(tag.getId(), tag.getContents()));
        }

        // 체크리스트
        List<JobCheckList> checkLists = jobCheckListDao.findByJobId(id);
        List<JobDetailJobChecklist> responseChecklists = new ArrayList<>();
        for (JobCheckList checkList : checkLists)
            responseChecklists.add(new JobDetailJobChecklist(checkList.getCheckListId(), checkList.getContents()));


        //worker라면 addressDetail이 HIRED...,COMPLETED 일 경우에만 나와야한다.
        List<String> accessAddressDetailStatus = new ArrayList<>();
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        accessAddressDetailStatus.add(ApplicationTypeEnum.COMPLETED.name());

        boolean accessAddressDetail = true;
        String jobStatus = applicationDao.findByJobIdAndUserId(id, userId) == null ? "FAILED" : applicationDao.findByJobIdAndUserId(id, userId).getStatus();

        if (userType.equals("worker") && !accessAddressDetailStatus.contains(jobStatus))
            accessAddressDetail = false;

        JobDetailCoordinate coordinate = new JobDetailCoordinate(job.get().getLat(), job.get().getLng());
        Optional<JobDetailJob> responseJob = Optional.of(new JobDetailJob(job.get().getId(), job.get().getTitle(), job.get().getStartAt(),
                job.get().getEndAt(), coordinate, job.get().getAddress(), accessAddressDetail ? job.get().getAddressDetail() : null,
                job.get().getPersonnel(), job.get().getPayType(), job.get().getPay(), job.get().getContents(), job.get().getStatus(),
                responseUser.get(), responseBenefits, responseTags, responseChecklists));

        //본인이 지원했는지
        Application application = applicationDao.findByJobIdAndUserId(job.get().getId(), userId);
        boolean didApply = !Objects.isNull(application);

        JobDetailPayload payload = new JobDetailPayload(responseJob.get(), didApply);
        return new JobDetailResponse(payload, new BasicMeta(true, ""));
    }

    public HiredListResponse getHiredList(long id, String accessToken) {
        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(ApplicationTypeEnum.HIRED.name());
        status.add(ApplicationTypeEnum.HIRED_REQUEST.name());
        status.add(ApplicationTypeEnum.HIRED_APPROVED.name());
        status.add(ApplicationTypeEnum.COMPLETED.name());

        List<Application> applications = applicationDao.findByJobIdAndStatusIn(id, status);

        List<HiredListApplication> responseApplications = new ArrayList<>();
        List<JobCheckList> jobCheckLists = jobCheckListDao.findByJobId(id);
        for (Application application : applications) {

            // user
            Optional<User> user = userDao.findByIdAndDeleteYn(application.getUserId(), "N");

            int experienceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            Optional<HiredListWorker> responseUser = Optional.of(new HiredListWorker(new HiredListUser(user.get().getId(), user.get().getName(),
                    user.get().getProfileImg()), experienceCount, absenceCount));

            Boolean isAction = false;
            if (application.getStatus().equals(ApplicationTypeEnum.HIRED.name())) {
                isAction = true;
            }

            // 지원서 체크리스트
            List<ApplicationChecklist> applicationChecklists = applicationChecklistDao.findByApplicationId(application.getId());
            List<HiredListApplicationChecklist> responseApplicationChecklists = new ArrayList<>();
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
                responseApplicationChecklists.add(new HiredListApplicationChecklist(jobCheckList.getContents(), isChecked));
            }

            Optional<HiredListApplication> responseApplication = Optional.of(new HiredListApplication(
                    application.getId(), application.getStatus(), responseUser.get(), responseApplicationChecklists));

            responseApplications.add(responseApplication.get());
        }
        Pagination pagination = new Pagination(true, 1, 20);     //TODO: 정상로직으로 수정필요 빌드만 되도록 수정해 놓음
        HiredListPayload payload = new HiredListPayload(responseApplications, pagination);
        HiredListMeta meta = new HiredListMeta(true, "", applications.size());

        return new HiredListResponse(payload, meta);
    }

    public AppliedListResponse getAppliedList(long id, AppliedList appliedList, String accessToken) {
        long userId = identification.getHeadertoken(accessToken);

        PageRequest pageRequest = PageRequest.of(appliedList.getPage(), appliedList.getPageSize());
        Page<Application> applications = applicationDao.findByJobIdAndApplied(id, ApplicationTypeEnum.APPLIED.name(), pageRequest);


        List<AppliedListApplication> responseApplications = new ArrayList<>();
        List<JobCheckList> jobCheckLists = jobCheckListDao.findByJobId(id);

        for (Application application : applications) {
            // user
            Optional<User> user = userDao.findByIdAndDeleteYn(application.getUserId(), "N");

            int experienceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.get().getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            Optional<AppliedListWorker> responseUser = Optional.of(new AppliedListWorker(new AppliedListUser(user.get().getId(), user.get().getName(),
                    user.get().getProfileImg()), experienceCount, absenceCount));

            // 지원서 체크리스트
            List<ApplicationChecklist> applicationChecklists = applicationChecklistDao.findByApplicationId(application.getId());
            List<AppliedListApplicationChecklist> responseApplicationChecklists = new ArrayList<>();

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
                responseApplicationChecklists.add(new AppliedListApplicationChecklist(jobCheckList.getContents(), isChecked));
            }

            Optional<AppliedListApplication> responseApplication = Optional.of(new AppliedListApplication(
                    application.getId(), application.getStatus(), responseUser.get(), responseApplicationChecklists));

            responseApplications.add(responseApplication.get());
        }
//        new APIJobClusterDetail.Pagination(job.isLast(), job.getTotalPages(), jobDetail.getPage());

        AppliedListPagination pagination = new AppliedListPagination(applications.isLast(), applications.getTotalPages(), appliedList.getPage());
        AppliedListPayload payload = new AppliedListPayload(responseApplications, pagination);
        AppliedListMeta meta = new AppliedListMeta(true, "");

        return new AppliedListResponse(payload, meta);
    }

    public ReceiptGiverResponse getReceiptGiver(long id) {
        List<RgApplicationOverview> responseApplications = new ArrayList<>();
        List<Application> applications = applicationDao.findByJobIdAndStatus(id, ApplicationTypeEnum.COMPLETED.name()); // 해당 공고에 일이 완료된 지원서 가져오기

        Job job = jobDao.findById(id).orElseThrow();

        for (Application application : applications) {

            // worker
            User user = userDao.findById(application.getUserId()).orElseThrow();
            RgUser responseUser = new RgUser(user.getId(), user.getName(),user.getProfileImg());

            // 지원서에 리뷰 작성했는지 체크
            boolean isReviewed = reviewDao.existsByGiverIdAndWorkerIdAndJobIdAndReviewType(job.getUserId(),application.getUserId(),job.getId(),"GIVER");
            int experienceCount = applicationDao.countByUserIdAndStatus(user.getId(), ApplicationTypeEnum.COMPLETED.name()); // 경력
            int absenceCount = applicationDao.countByUserIdAndStatus(user.getId(), ApplicationTypeEnum.NO_SHOW.name()); // 결근

            RgWorker responseWorker = new RgWorker(responseUser, experienceCount, absenceCount);

            // payment 계산
            int payment = applicationTotalPaymentDao.getByApplicationId(application.getId()).getTotalPayment();
            responseApplications.add(new RgApplicationOverview(application.getId(), responseWorker, application.getStartAt(), application.getEndAt(), payment, isReviewed));
        }

        RgPayload payload = new RgPayload(responseApplications);
        BasicMeta meta = new BasicMeta(true, "");

        return new ReceiptGiverResponse(payload, meta);
    }

    public PreviousJobResponse getPreviousJobs(String accessToken) {
        long userId = identification.getHeadertoken(accessToken);

        List<PreviousJob> responseJobs = new ArrayList<>();
        List<Job> jobs = jobDao.findTop10ByUserIdOrderByIdDesc(userId); // 상위 10개 공고

        for (Job job : jobs) {
            List<JobCheckList> jobChecklist = jobCheckListDao.findByJobId(job.getId());
            // List<String> checklistContents = checklist.stream().map(JobCheckList::getContents).collect(Collectors.toList());
            List<String> jobChecklistContents = new ArrayList<>();
            for (JobCheckList checklist : jobChecklist) {
                jobChecklistContents.add(checklist.getContents());
            }

            List<JobTag> jobTag = jobTagDao.findByJobId(job.getId());
            List<String> jobTagContents = new ArrayList<>();
            for (JobTag tag : jobTag) {
                jobTagContents.add(tag.getContents());
            }

            Optional<PreviousJob> responseJob = Optional.of(new PreviousJob(
                    job.getTitle(), job.getStartAt(), job.getEndAt(), job.getPayType(), job.getPay(), job.getContents(),
                    job.getCreatedAt(), job.getLat(), job.getLng(), job.getAddressDetail(), jobChecklistContents, jobTagContents, job.getPersonnel()));

            responseJobs.add(responseJob.get());
        }

        JobPreviousPayload payload = new JobPreviousPayload(responseJobs);
        BasicMeta meta = new BasicMeta(true, "");

        return new PreviousJobResponse(payload, meta);
    }

    public ProceedingListResponse getProceedingList(String accessToken, Integer page) {
        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(JobTypeEnum.OPEN.name());
        status.add(JobTypeEnum.STARTED.name());
        status.add(JobTypeEnum.CLOSED.name());

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Job> jobs = jobDao.findByUserIdAndStatusIn(userId, status, pageRequest);
        List<ProceedingJob> responseJobs = new ArrayList<>();

        for (Job job : jobs) {

            //가장 많은 application이며 가장 높은 status 우선순위로 나옴.
            ProceedingGroupStatus proceedingGroupStatus = new ProceedingGroupStatus();
            GroupStatus groupStatus = applicationDao.processingGroupStatus(job.getId());

            if (groupStatus != null) {
                proceedingGroupStatus.setName(groupStatus.getName());
                proceedingGroupStatus.setCount(groupStatus.getCount());
            }

            // payment 계산
            int payment = 0;
            List<ApplicationTotalPayment> applicationTotalPaymentList = applicationTotalPaymentDao.getByJobId(job.getId());

            for (ApplicationTotalPayment app : applicationTotalPaymentList)
                payment += app.getTotalPayment();

            //tags
            List<JobTag> tags = jobTagDao.findByJobId(job.getId());

            ProceedingJob responseJob = Optional.of(new ProceedingJob(
                    job.getId(), job.getTitle(), job.getStartAt(), job.getEndAt(), proceedingGroupStatus, job.getStatus(), payment, tags)).orElseThrow();

            responseJobs.add(responseJob);
        }

        Pagination pagination = new Pagination(jobs.isLast(), page, jobs.getTotalPages());
        JobProceedingPayload jobProceedingPayload = new JobProceedingPayload(responseJobs, pagination);
        BasicMeta meta = new BasicMeta(true, "");

        return new ProceedingListResponse(jobProceedingPayload, meta);
    }

    public CompletedListResponse getCompletedList(String accessToken, Integer page) {
        long userId = identification.getHeadertoken(accessToken);

        List<String> status = new ArrayList<>();
        status.add(JobTypeEnum.COMPLETED.name());

        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Job> jobs = jobDao.findByUserIdAndStatusIn(userId, status, pageRequest);
        List<CompletedJob> responseJobs = new ArrayList<>();

        for (Job job : jobs) {
            CompletedGroupStatus completedGroupStatus = new CompletedGroupStatus();
            GroupStatus groupStatus = applicationDao.completedGroupStatus(job.getId());

            if (groupStatus != null) {
                completedGroupStatus.setName(groupStatus.getName());
                completedGroupStatus.setCount(groupStatus.getCount());
            }
            // payment 계산
            int payment = 0;
            List<ApplicationTotalPayment> applicationTotalPaymentList = applicationTotalPaymentDao.getByJobId(job.getId());

            for (ApplicationTotalPayment app : applicationTotalPaymentList)
                payment += app.getTotalPayment();

            //tags
            List<JobTag> tags = jobTagDao.findByJobId(job.getId());

            Optional<CompletedJob> responseJob = Optional.of(new CompletedJob(
                    job.getId(), job.getTitle(), job.getStartAt(), job.getEndAt(), completedGroupStatus, job.getStatus(), payment, tags));

            responseJobs.add(responseJob.get());
        }

        Pagination pagination = new Pagination(jobs.isLast(), page, jobs.getTotalPages());
        JobCompletedPayload payload = new JobCompletedPayload(responseJobs, pagination);
        BasicMeta meta = new BasicMeta(true, "");

        return new CompletedListResponse(payload, meta);
    }

    public SuccessResponse updateStatusClosed(long id) {
        Optional<Job> job = jobDao.findById(id);

        if (job.isEmpty())
            return new SuccessResponse(new BasicMeta(false, "공고가존재하지않습니다."));

        if (applicationDao.findByJobIdAndStatus(id, ApplicationTypeEnum.HIRED.name()).size() > 0)
            job.get().setStatus(JobTypeEnum.CLOSED.name());
        else
            job.get().setStatus(JobTypeEnum.FAILED.name());
        jobDao.save(job.get());

        return new SuccessResponse(new BasicMeta(true, "성공적으로 변경하였습니다."));
    }

    public GetMinimumWageResponse getMinimumWage() {
        GetMinimumWagePayload payload = new GetMinimumWagePayload(9620);
        final BasicMeta meta =  new BasicMeta(true, "");
        return new GetMinimumWageResponse(payload, meta);
    }

    public JobHiredInfoResponse getJobHiredInfo(long jobId, long applicationId) {
        Optional<Job> jobOptional = jobDao.findById(jobId);

        if (jobOptional.isEmpty()) {
            throw new NotFoundException("존재하지 않는 공고입니다.");
        }

        Job job = jobOptional.get();
        List<JobTag> jobTagList = jobTagDao.findByJobId(job.getId());
        Application application = applicationDao.findById(applicationId).orElseThrow();
        long workerId = application.getUserId();
        User worker = userDao.findById(workerId).get();
        User giver = userDao.findById(job.getUserId()).get();

        return JobHiredInfoResponse.builder()
                .payload(JobHiredInfoPayload.of(job, jobTagList, worker, giver, application))
                .meta(new BasicMeta(true, ""))
                .build();

    }
}

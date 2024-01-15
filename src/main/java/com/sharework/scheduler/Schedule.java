package com.sharework.scheduler;

import com.sharework.common.ApplicationTypeEnum;
import com.sharework.common.JobTypeEnum;
import com.sharework.dao.ApplicationDao;
import com.sharework.dao.ApplicationTotalPaymentDao;
import com.sharework.dao.JobDao;
import com.sharework.model.Application;
import com.sharework.model.ApplicationTotalPayment;
import com.sharework.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Schedule {

    private final JobDao jobDao;

    private final ApplicationDao applicationDao;

    private final ApplicationTotalPaymentDao applicationTotalPaymentDao;

    @Transactional
    @Scheduled(cron = "0 0/1 * * * ?", zone = "Asia/Seoul")
    public void update() throws Exception {
        startAtAutoUpdate();
        endAtAutoUpdate();
    }

    private void startAtAutoUpdate() {
        //시작시간이 현재시간을 넘긴 공고(status = CLOSED)에서 지원한 지원서 APPLIED -> FAILED
        List<Application> applicationList = applicationDao.getStartedStatus();
        applicationList.forEach(item -> item.setStatus(ApplicationTypeEnum.FAILED.name()));

        //시작시간이 현재시간을 넘은 OPEN,CLOSED인 공고 OPEN 중 %HIRED%가 있다면 STARTED, 없다면 failed
        List<Job> jobList = jobDao.getStartTimeJobs();
        jobList.forEach(item -> {
            if (applicationDao.countByJobIdAndStatusContaining(item.getId(), ApplicationTypeEnum.HIRED.name()) > 0) {
                item.setStatus(JobTypeEnum.STARTED.name());
            } else
                item.setStatus(JobTypeEnum.FAILED.name());
        });
    }

    private void updateEndAtApplicationStatus() {
        //application schedule

        //status가 appied이며, 현재시간이 마감시간을 넘겼다면 failed
        List<Application> applicationAppliedList = applicationDao.findByEndAtLessThanEqualAndStatusEqualsOrderByEndAtAsc(
                LocalDateTime.now(), ApplicationTypeEnum.APPLIED.name()
        );
        applicationAppliedList.forEach(item -> item.setStatus(ApplicationTypeEnum.FAILED.name()));

        //status가 hired_request이며, 현재시간이 마감시간을 넘겼다면 rejected
        List<Application> applicationHiredRequestList = applicationDao.findByEndAtLessThanEqualAndStatusEqualsOrderByEndAtAsc(
                LocalDateTime.now(), ApplicationTypeEnum.HIRED_REQUEST.name()
        );
        applicationHiredRequestList.forEach(item -> item.setStatus(ApplicationTypeEnum.REJECTED.name()));

        //status가 hired_approved, 현재시간이 마감시간을 넘겼다면 completed
        List<Application> applicationHiredApprovedList = applicationDao.findByEndAtLessThanEqualAndStatusEqualsOrderByEndAtAsc(
                LocalDateTime.now(), ApplicationTypeEnum.HIRED_APPROVED.name()
        );
        applicationHiredApprovedList.forEach(item -> {
            item.setStatus(ApplicationTypeEnum.COMPLETED.name());

            Job job = jobDao.getById(item.getJobId());
            int totalPayment = 0;

            if (job.getPayType().equals("일급"))
                totalPayment = job.getPay();
            else {
                double hourly = job.getPay();
                LocalDateTime startAt = LocalDateTime.parse(item.getStartAt().toString());
                LocalDateTime endAt = LocalDateTime.parse(item.getEndAt().toString());
                Duration duration = Duration.between(startAt, endAt);
                long minutes = duration.toMinutes();
                totalPayment = (int) Math.ceil(((hourly / 60) * minutes) / 10.0) * 10;
            }
            applicationTotalPaymentDao.save(ApplicationTotalPayment.builder().applicationId(item.getId()).jobId(item.getJobId()).totalPayment(totalPayment).build());
        });


        //status가 hired이며, 현재시간이 마감시간을 넘겼다면 noshow
        List<Application> applicationHiredList = applicationDao.findByEndAtLessThanEqualAndStatusEqualsOrderByEndAtAsc(
                LocalDateTime.now(), ApplicationTypeEnum.HIRED.name());
        applicationHiredList.forEach(item -> item.setStatus(ApplicationTypeEnum.NO_SHOW.name()));
    }

    private void endAtAutoUpdate() {
        //job schedule
        //status가 started이며, 현재시간이 마감시간을 넘겼다면 COMPLTED 시간으로 돈 계산하여 application_total_payment에 저장한다.
        List<Job> jobStartedList = jobDao.getEndTimeoutStartedJobs();
        jobStartedList.forEach(item -> {
            item.setStatus(JobTypeEnum.COMPLETED.name());
        });

        //status가 open,closed이며, 현재시간이 마감시간을 넘겼다면 falied
        List<Job> jobOpenAndClosedList = jobDao.getEndTimeoutOpenAndClosedJobs();
        jobOpenAndClosedList.forEach(item -> item.setStatus(JobTypeEnum.FAILED.name()));

        updateEndAtApplicationStatus();
    }
}

package com.sharework.response.model.job;

import com.sharework.model.Application;
import com.sharework.model.Job;
import com.sharework.model.JobTag;
import com.sharework.model.User;
import com.sharework.response.model.user.GiverNameAndPhoneNumber;
import com.sharework.response.model.user.WorkerNameAndPhoneNumber;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JobHiredInfoPayload {

    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String address;
    private String addressDetail;
    private String contents;
    private List<String> jobTagList;
    private WorkerNameAndPhoneNumber workerNameAndPhoneNumber;
    private GiverNameAndPhoneNumber giverNameAndPhoneNumber;

    @Builder
    public JobHiredInfoPayload(String title, LocalDateTime startAt, LocalDateTime endAt, String address, String addressDetail, String contents, List<String> jobTagList, WorkerNameAndPhoneNumber workerNameAndPhoneNumber, GiverNameAndPhoneNumber giverNameAndPhoneNumber) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.address = address;
        this.addressDetail = addressDetail;
        this.contents = contents;
        this.jobTagList = jobTagList;
        this.workerNameAndPhoneNumber = workerNameAndPhoneNumber;
        this.giverNameAndPhoneNumber = giverNameAndPhoneNumber;
    }

    public static JobHiredInfoPayload of(Job job, List<JobTag> jobTagList, User worker, User giver, Application application) {
        return JobHiredInfoPayload.builder()
                .title(job.getTitle())
                .startAt(job.getStartAt())
                .endAt(job.getEndAt())
                .address(job.getAddress())
                .addressDetail(job.getAddressDetail())
                .contents(job.getContents())
                .jobTagList(jobTagList.stream().map(JobTag::getContents).collect(Collectors.toList()))
                .workerNameAndPhoneNumber(WorkerNameAndPhoneNumber.builder()
                        .name(worker.getName())
                        .phoneNumber(worker.getPhoneNumber())
                        .userProfileImg(worker.getProfileImg())
                        .status(application.getStatus())
                        .build())
                .giverNameAndPhoneNumber(GiverNameAndPhoneNumber.builder()
                        .name(giver.getName())
                        .phoneNumber(giver.getPhoneNumber())
                        .userProfileImg(giver.getProfileImg())
                        .build())
                .build();
    }

}

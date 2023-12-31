package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.model.JobTag;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProceedingListResponse {
    public ProceedingListResponse(JobProceedingPayload jobProceedingPayload, BasicMeta meta) {
        this.payload = jobProceedingPayload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public JobProceedingPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class JobProceedingPayload {
        @ApiModelProperty(value = "jobOverviews")
        public List<ProceedingJob> jobOverviews;

        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public JobProceedingPayload(List<ProceedingJob> jobOverviews, Pagination pagination) {
            this.jobOverviews = jobOverviews;
            this.pagination = pagination;
        }
    }

    public static class ProceedingJob {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "title")
        public String title;

        @Column(name = "start_at")
        public LocalDateTime startAt;

        @Column(name = "end_at")
        public LocalDateTime endAt;

        public long totalPay;

        public ProceedingGroupStatus groupStatus;

        @Column(name = "status")
        public String status;

        public List<JobTag> tags;

        public ProceedingJob(long id, String title, LocalDateTime startAt, LocalDateTime endAt, ProceedingGroupStatus groupStatus, String status, long totalPay, List<JobTag> tags) {
            this.id = id;
            this.title = title;
            this.startAt = startAt;
            this.endAt = endAt;
            this.groupStatus = groupStatus;
            this.status = status;
            this.totalPay = totalPay;
            this.tags = tags;
        }
    }

    @RequiredArgsConstructor
    @Data
    public static class ProceedingGroupStatus {

        String name = "APPLIED";
        int count = 0;
    }
}




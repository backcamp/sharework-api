package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.model.JobTag;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompletedListResponse {
    public CompletedListResponse(JobCompletedPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public JobCompletedPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class JobCompletedPayload {
        @ApiModelProperty(value = "jobOverviews")
        public List<CompletedJob> jobOverviews;

        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public JobCompletedPayload(List<CompletedJob> jobOverviews, Pagination pagination) {
            this.jobOverviews = jobOverviews;
            this.pagination = pagination;
        }
    }

    public static class CompletedJob {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "title")
        public String title;

        @Column(name = "start_at")
        public LocalDateTime startAt;

        @Column(name = "end_at")
        public LocalDateTime endAt;

        public CompletedGroupStatus groupStatus;

        public long totalPay;

        @Column(name = "status")
        public String status;

        public List<JobTag> tags;

        public CompletedJob(long id, String title, LocalDateTime startAt, LocalDateTime endAt, CompletedGroupStatus groupStatus, String status, long totalPay, List<JobTag> tags) {
            this.id = id;
            this.title = title;
            this.startAt = startAt;
            this.groupStatus = groupStatus;
            this.endAt = endAt;
            this.totalPay = totalPay;
            this.status = status;
            this.tags = tags;
        }
    }

    @RequiredArgsConstructor
    @Data
    public static class CompletedGroupStatus {

        String name = "COMPLETED";
        int count = 0;
    }
}




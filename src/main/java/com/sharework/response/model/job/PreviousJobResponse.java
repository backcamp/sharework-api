package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreviousJobResponse {
    public PreviousJobResponse(JobPreviousPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public JobPreviousPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class JobPreviousPayload {
        @ApiModelProperty(value = "jobs")
        public List<PreviousJob> jobs;

        public JobPreviousPayload(List<PreviousJob> jobs) {
            this.jobs = jobs;
        }
    }

    public static class PreviousJob {

        @Column(name = "title")
        public String title;

        @Column(name = "startAt")
        public LocalDateTime startAt;

        @Column(name = "endAt")
        public LocalDateTime endAt;

        @Column(name = "payType")
        public String payType;

        @Column(name = "pay")
        public Integer pay;

        @Column(name = "contents")
        public String contents;

        @Column(name = "createdAt")
        public LocalDateTime createdAt;

        public double lat;

        public double lng;

        public String addressDetail;

        public List<String> checklist;

        public List<String> tags;

        public long personnel;

        public PreviousJob(String title, LocalDateTime startAt, LocalDateTime endAt, String payType, Integer pay, String contents, LocalDateTime createdAt, double lat, double lng,
                   String addressDetail, List<String> checklist, List<String> tags, long personnel) {
            this.title = title;
            this.startAt = startAt;
            this.endAt = endAt;
            this.payType = payType;
            this.pay = pay;
            this.contents = contents;
            this.createdAt = createdAt;
            this.lat = lat;
            this.lng = lng;
            this.addressDetail = addressDetail;
            this.checklist = checklist;
            this.tags = tags;
            this.personnel = personnel;
        }
    }
}




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
public class ReceiptGiverResponse {
    public ReceiptGiverResponse(RgPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public RgPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class RgPayload {
        @ApiModelProperty(value = "applicationOverviews")
        public List<RgApplicationOverview> applicationOverviews;

        public RgPayload(List<RgApplicationOverview> applicationOverviews) {
            this.applicationOverviews = applicationOverviews;
        }
    }

    public static class RgApplicationOverview {
        @ApiModelProperty(value = "id")
        public long id;

        @ApiModelProperty(value = "worker")
        public RgWorker worker;

        @ApiModelProperty(value = "startAt")
        public LocalDateTime startAt;

        @ApiModelProperty(value = "endAt")
        public LocalDateTime endAt;

        @Column(name = "totalPay")
        public Integer totalPay;

        @ApiModelProperty(value = "isReview")
        public boolean isReviewed;

        public RgApplicationOverview(long id, RgWorker worker, LocalDateTime startAt, LocalDateTime endAt, int totalPay, boolean isReviewed) {
            this.id = id;
            this.worker = worker;
            this.startAt = startAt;
            this.endAt = endAt;
            this.totalPay = totalPay;
            this.isReviewed=isReviewed;
        }
    }

    public static class RgWorker {
        @ApiModelProperty(value = "user")
        public RgUser user;
        @ApiModelProperty(value = "experienceCount")
        public int experienceCount;
        @ApiModelProperty(value = "absenceCount")
        public int absenceCount;

        public RgWorker(RgUser user, int experienceCount, int absenceCount) {
            this.user = user;
            this.experienceCount = experienceCount;
            this.absenceCount = absenceCount;
        }
    }


    public static class RgUser {
        @ApiModelProperty(value = "id")
        public long id;

        @ApiModelProperty(value = "name")
        public String name;

        @ApiModelProperty(value = "profileImg")
        public String profileImg;

        public RgUser(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }
}




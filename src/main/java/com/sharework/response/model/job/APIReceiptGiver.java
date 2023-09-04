package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIReceiptGiver {
    public APIReceiptGiver(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public class Payload {
        @ApiModelProperty(value = "applicationOverviews")
        public List<ApplicationOverview> applicationOverviews;

        public Payload(List<ApplicationOverview> applicationOverviews) {
            this.applicationOverviews = applicationOverviews;
        }
    }

    public static class ApplicationOverview {
        @ApiModelProperty(value = "id")
        public long id;

        @ApiModelProperty(value = "worker")
        public Worker worker;

        @ApiModelProperty(value = "startAt")
        public LocalDateTime startAt;

        @ApiModelProperty(value = "endAt")
        public LocalDateTime endAt;

        @Column(name = "totalPay")
        public Integer totalPay;

        @ApiModelProperty(value = "status")
        public String status;

        public ApplicationOverview(long id, Worker worker, LocalDateTime startAt, LocalDateTime endAt, int totalPay, String status) {
            this.id = id;
            this.worker = worker;
            this.startAt = startAt;
            this.endAt = endAt;
            this.totalPay = totalPay;
            this.status = status;
        }
    }

    public static class Worker {
        @ApiModelProperty(value = "user")
        public User user;
        @ApiModelProperty(value = "experienceCount")
        public int experienceCount;
        @ApiModelProperty(value = "absenceCount")
        public int absenceCount;

        public Worker(User user, int experienceCount, int absenceCount) {
            this.user = user;
            this.experienceCount = experienceCount;
            this.absenceCount = absenceCount;
        }
    }


    public static class User {
        @ApiModelProperty(value = "id")
        public long id;

        @ApiModelProperty(value = "name")
        public String name;

        @ApiModelProperty(value = "profileImg")
        public String profileImg;

        public User(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }
}




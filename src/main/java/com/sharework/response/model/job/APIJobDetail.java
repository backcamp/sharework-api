package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class APIJobDetail {

    public APIJobDetail(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class Payload {
        @ApiModelProperty(value = "jobDetail")
        public Job jobDetail;

        public boolean didApply;

        public Payload(Job jobDetail, boolean didApply) {
            this.jobDetail = jobDetail;
            this.didApply = didApply;
        }
    }

    public static class Job {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "title")
        public String title;

        @Column(name = "status")
        public String status;

        public Coordinate coordinate;

        @ApiModelProperty(value = "user")
        public User giver;

        @Column(name = "start_at")
        public LocalDateTime startAt;

        @Column(name = "end_at")
        public LocalDateTime endAt;

        @ApiModelProperty(value = "recruitNumber")
        public long recruitNumber;

        @Column(name = "pay")
        public int pay;

        @Column(name = "pay_type")
        public String payType;

        @ApiModelProperty(value = "isSameDayPayment")
        public boolean isSameDayPayment;

        @ApiModelProperty(value = "tags")
        public List<JobTag> tags;

        @ApiModelProperty(value = "checkList")
        public List<JobChecklist> checkList;

        @Column(name = "description")
        public String description;

        @Column(name = "address")
        public String address;

        @Column(name = "address_detail")
        public String addressDetail;


        @ApiModelProperty(value = "jobBenefits")
        public List<JobBenefit> jobBenefits;


        public Job(long id, String title, LocalDateTime startAt, LocalDateTime endAt, Coordinate coordinate,
                   String address, String addressDetail, long recruitNumber, String payType, int pay, String contents,
                   String status, User giver, List<JobBenefit> jobBenefits, List<JobTag> tags, List<JobChecklist> jobChecklists) {
            this.id = id;
            this.title = title;
            this.coordinate = coordinate;
            this.status = status;
            this.giver = giver;
            this.startAt = startAt;
            this.endAt = endAt;
            this.recruitNumber = recruitNumber;
            this.pay = pay;
            this.payType = payType;
            this.isSameDayPayment = false;
            this.tags = tags;
            this.checkList = jobChecklists;
            this.description = contents;
            this.address = address;
            this.addressDetail = addressDetail;
            this.jobBenefits = jobBenefits;
        }
    }

    public static class User {

        public long id;

        @Column(name = "name")
        public final String name;

        @Column(name = "profile_mg")
        public String profileImg;

        public User(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }

    public static class JobBenefit {
        @Column(name = "contents")
        public String contents;

        public JobBenefit(String contents) {
            this.contents = contents;
        }
    }

    public static class JobTag {
        @Column(name = "id")
        public long id;

        @Column(name = "contents")
        public String contents;

        public JobTag(long id, String contents) {
            this.id = id;
            this.contents = contents;
        }
    }

    public static class JobChecklist {
        @Column(name = "id")
        public long id;

        @Column(name = "contents")
        public String contents;

        public JobChecklist(long id, String contents) {
            this.id = id;
            this.contents = contents;
        }
    }

    public static class Coordinate {
        @Column(name = "lat")
        public double latitude;

        @Column(name = "lng")
        public double longitude;

        public Coordinate(double lat, double lng) {
            this.latitude = lat;
            this.longitude = lng;
        }
    }
}




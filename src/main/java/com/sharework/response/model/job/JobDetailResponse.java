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
public class JobDetailResponse {

    public JobDetailResponse(JobDetailPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public JobDetailPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class JobDetailPayload {
        @ApiModelProperty(value = "jobDetail")
        public JobDetailJob jobDetail;

        public boolean didApply;

        public JobDetailPayload(JobDetailJob jobDetail, boolean didApply) {
            this.jobDetail = jobDetail;
            this.didApply = didApply;
        }
    }

    public static class JobDetailJob {

        @Id
        @Column(name = "id")
        public long id;

        @Column(name = "title")
        public String title;

        @Column(name = "status")
        public String status;

        public JobDetailCoordinate coordinate;

        @ApiModelProperty(value = "user")
        public JobDetailUser giver;

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
        public List<JobDetailJobTag> tags;

        @ApiModelProperty(value = "checkList")
        public List<JobDetailJobChecklist> checkList;

        @Column(name = "description")
        public String description;

        @Column(name = "address")
        public String address;

        @Column(name = "address_detail")
        public String addressDetail;


        @ApiModelProperty(value = "jobBenefits")
        public List<JobDetailJobBenefit> jobBenefits;


        public JobDetailJob(long id, String title, LocalDateTime startAt, LocalDateTime endAt, JobDetailCoordinate coordinate,
                   String address, String addressDetail, long recruitNumber, String payType, int pay, String contents,
                   String status, JobDetailUser giver, List<JobDetailJobBenefit> jobBenefits, List<JobDetailJobTag> tags, List<JobDetailJobChecklist> jobChecklists) {
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

    public static class JobDetailUser {

        public long id;

        @Column(name = "name")
        public final String name;

        @Column(name = "profile_mg")
        public String profileImg;

        public JobDetailUser(long id, String name, String profileImg) {
            this.id = id;
            this.name = name;
            this.profileImg = profileImg;
        }
    }

    public static class JobDetailJobBenefit {
        @Column(name = "contents")
        public String contents;

        public JobDetailJobBenefit(String contents) {
            this.contents = contents;
        }
    }

    public static class JobDetailJobTag {
        @Column(name = "id")
        public long id;

        @Column(name = "contents")
        public String contents;

        public JobDetailJobTag(long id, String contents) {
            this.id = id;
            this.contents = contents;
        }
    }

    public static class JobDetailJobChecklist {
        @Column(name = "id")
        public long id;

        @Column(name = "contents")
        public String contents;

        public JobDetailJobChecklist(long id, String contents) {
            this.id = id;
            this.contents = contents;
        }
    }

    public static class JobDetailCoordinate {
        @Column(name = "lat")
        public double latitude;

        @Column(name = "lng")
        public double longitude;

        public JobDetailCoordinate(double lat, double lng) {
            this.latitude = lat;
            this.longitude = lng;
        }
    }
}




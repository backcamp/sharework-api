package com.sharework.response.model.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.job.JobOverview;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIApplicationHistory {
    public APIApplicationHistory(AhPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public AhPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class AhPayload {
        @ApiModelProperty(value = "applications")
        public List<AhApplication> applications;

        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public AhPayload(List<AhApplication> applications, Pagination pagination) {
            this.applications = applications;
            this.pagination = pagination;
        }
    }

    public static class AhApplication {

        @ApiModelProperty(value = "id")
        public long id;

        @ApiModelProperty(value = "status")
        public String status;

        @ApiModelProperty(value = "jobOverview")
        public JobOverview jobOverview;

        @ApiModelProperty(value = "isRequestPossible")
        public boolean isRequestPossible;

        @ApiModelProperty(value = "isReviewed")
        public boolean isReviewed;

        public AhApplication(long id, String status, JobOverview jobOverview, boolean isRequestPossible,boolean isReviewed) {
            this.id = id;
            this.status = status;
            this.jobOverview = jobOverview;
            this.isRequestPossible = isRequestPossible;
            this.isReviewed = isReviewed;
        }
    }
}




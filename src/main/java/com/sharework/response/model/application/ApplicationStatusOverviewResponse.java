package com.sharework.response.model.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationStatusOverviewResponse {
    public ApplicationStatusOverviewResponse(ApplicationStatusOverviewPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public ApplicationStatusOverviewPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class ApplicationStatusOverviewPayload {

        public StatusOverview statusOverview;

        public ApplicationStatusOverviewPayload(StatusOverview statusOverview) {
            this.statusOverview = statusOverview;
        }
    }

    public static class StatusOverview {

        @ApiModelProperty(value = "appliedCount")
        public int appliedCount;

        @ApiModelProperty(value = "hiredCount")
        public int hiredCount;

        public StatusOverview(int appliedCount, int hiredCount) {
            this.appliedCount = appliedCount;
            this.hiredCount = hiredCount;
        }
    }


}




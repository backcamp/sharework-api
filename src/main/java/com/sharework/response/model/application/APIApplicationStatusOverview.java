package com.sharework.response.model.application;

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
public class APIApplicationStatusOverview {
    public APIApplicationStatusOverview(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class Payload {

        public StatusOverview statusOverview;

        public Payload(StatusOverview statusOverview) {
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




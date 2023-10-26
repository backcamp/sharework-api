package com.sharework.response.model.job;

import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel
@Getter
@Setter
public class JobHiredInfoResponse {

    @ApiModelProperty(value = "payload", position = 1)
    private JobHiredInfoPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    private BasicMeta meta;


    @Builder
    public JobHiredInfoResponse(JobHiredInfoPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }
}

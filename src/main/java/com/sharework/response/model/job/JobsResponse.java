package com.sharework.response.model.job;

import com.sharework.response.model.BasicResponse;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class JobsResponse {

    public JobsResponse(JobsPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    private JobsPayload payload;
    @ApiModelProperty(value = "meta", position = 2)
    private BasicMeta meta;
}

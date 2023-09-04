package com.sharework.response.model;

import com.sharework.response.model.application.ApplicationPayload;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class ApplicationResponse extends BasicResponse {

    public ApplicationResponse(boolean status, ApplicationPayload payload, Pagination pagination) {
        super(status);
        this.payload = payload;
        this.pagination = pagination;
    }

    @ApiModelProperty(value = "payload", position = 2)
    private ApplicationPayload payload;

    @ApiModelProperty(value = "pagination", position = 3)
    private Pagination pagination;
}

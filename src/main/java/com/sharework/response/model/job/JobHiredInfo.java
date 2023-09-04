package com.sharework.response.model.job;

import com.sharework.model.Job;
import com.sharework.model.JobTag;
import com.sharework.model.User;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.user.GiverNameAndPhoneNumber;
import com.sharework.response.model.user.WorkerNameAndPhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Getter
@Setter
public class JobHiredInfo {

    @ApiModelProperty(value = "payload", position = 1)
    private JobHiredInfoPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    private BasicMeta meta;


    @Builder
    public JobHiredInfo(JobHiredInfoPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }
}

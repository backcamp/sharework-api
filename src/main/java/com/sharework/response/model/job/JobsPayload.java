package com.sharework.response.model.job;

import java.util.List;

import com.sharework.response.model.MainJobsResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
public class JobsPayload {
    @ApiModelProperty(value = "jobLocations", position = 1)
    private List<MainJobsResponse> jobLocations;

}

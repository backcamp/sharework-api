package com.sharework.response.model;

import com.sharework.response.model.job.JobPayload;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobStatusResponse {

    private JobPayload jobs;

    private Pagination pagination;

}
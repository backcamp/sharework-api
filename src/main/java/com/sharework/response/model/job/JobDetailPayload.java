package com.sharework.response.model.job;

import com.sharework.model.Job;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@ApiModel
@Getter
@Builder
@AllArgsConstructor
public class JobDetailPayload {
    private List<Job> jobs;

}
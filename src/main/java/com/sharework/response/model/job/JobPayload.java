package com.sharework.response.model.job;

import com.sharework.model.Job;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@ApiModel
@Getter
@Setter
@Builder
@AllArgsConstructor
public class JobPayload {
    private Page<Job> jobs;
}

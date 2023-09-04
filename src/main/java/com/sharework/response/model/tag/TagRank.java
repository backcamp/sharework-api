package com.sharework.response.model.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.job.JobTagList;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagRank {

    private JobTagList tag;
    private Integer count;
    private Integer hour;

    public TagRank(JobTagList tag, int count, int hour) {
        this.tag = tag;
        this.count = count;
        this.hour = hour;
    }

    public TagRank(JobTagList tag, int count) {
        this.tag = tag;
        this.count = count;
    }
}

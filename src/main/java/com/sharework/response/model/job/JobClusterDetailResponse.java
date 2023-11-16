package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobClusterDetailResponse {

    public JobClusterDetailResponse(JobClusterDetailPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public JobClusterDetailPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class JobClusterDetailPayload {
        @ApiModelProperty(value = "jobOverviews")
        public List<JobOverview> jobOverviews;

        @ApiModelProperty(value = "pagination")
        public JobClusterDetailPagination pagination;

        public JobClusterDetailPayload(List<JobOverview> jobOverviews, JobClusterDetailPagination pagination) {
            this.jobOverviews = jobOverviews;
            this.pagination = pagination;
        }
    }

    public static class JobClusterDetailPagination {
        @ApiModelProperty(value = "last")
        public boolean last;

        @ApiModelProperty(value = "totalPage")
        public int totalPage;

        @ApiModelProperty(value = "page")
        public int nextPage;

        @Builder
        public JobClusterDetailPagination(boolean last, int totalPage, int page) {
            this.last = last;
            this.totalPage = totalPage;
            this.nextPage = page;

        }

    }
}




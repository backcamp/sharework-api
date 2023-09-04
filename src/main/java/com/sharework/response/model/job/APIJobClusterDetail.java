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
public class APIJobClusterDetail {

    public APIJobClusterDetail(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class Payload {
        @ApiModelProperty(value = "jobOverviews")
        public List<JobOverview> jobOverviews;

        @ApiModelProperty(value = "pagination")
        public Pagination pagination;

        public Payload(List<JobOverview> jobOverviews, Pagination pagination) {
            this.jobOverviews = jobOverviews;
            this.pagination = pagination;
        }
    }

    public static class Pagination {
        @ApiModelProperty(value = "last")
        public boolean last;

        @ApiModelProperty(value = "totalPage")
        public int totalPage;

        @ApiModelProperty(value = "page")
        public int nextPage;

        @Builder
        public Pagination(boolean last, int totalPage, int page) {
            this.last = last;
            this.totalPage = totalPage;
            this.nextPage = page;

        }

    }
}




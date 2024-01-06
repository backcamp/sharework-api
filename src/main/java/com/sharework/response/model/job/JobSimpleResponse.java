package com.sharework.response.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobSimpleResponse {

    private JobSimplePayload payload;
    private BasicMeta meta;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JobSimplePayload {

        private JobSimple jobDetail;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JobSimple {

        private LocalDateTime endAt;
        private int pay;
        private LocalDateTime startAt;
        private String status;
        private List<JobDetailResponse.JobDetailJobTag> tags;
        private String title;
    }
}

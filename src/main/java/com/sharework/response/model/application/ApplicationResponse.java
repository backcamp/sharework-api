package com.sharework.response.model.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    private ApplicationPayload payload;
    private BasicMeta meta;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApplicationPayload {
        private ApplicationHistoryResponse.AhApplication application;
    }
}

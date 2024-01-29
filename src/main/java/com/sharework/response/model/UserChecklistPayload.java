package com.sharework.response.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
public class UserChecklistPayload {
	@ApiModelProperty(value = "checkList", position = 1)
	private Checklist checkList;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Checklist {
        private long userId;
        private String[] contents;
    }
}

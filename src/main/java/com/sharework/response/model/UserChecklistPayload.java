package com.sharework.response.model;

import java.util.List;

import com.sharework.model.UserChecklist;
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
	private UserChecklist checkList;

}

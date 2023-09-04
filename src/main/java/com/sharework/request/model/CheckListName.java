package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
@Data
@NoArgsConstructor
public class CheckListName {
	@ApiModelProperty(value = "checkListName", position = 1, example = "[test1,test2]", required = true)
	private String[] checkListName;
}

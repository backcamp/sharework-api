package com.sharework.response.model.meta;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicMeta {

	@ApiModelProperty(value = "status", position = 1)
	private boolean status;

	@ApiModelProperty(value = "message", position = 2)
	private String message;
}

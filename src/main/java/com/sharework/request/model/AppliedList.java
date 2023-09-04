package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppliedList {
	@ApiModelProperty(value = "page", position = 1, example = "0")
	private int page = 0;
	@ApiModelProperty(value = "pageSize", position = 2, example = "5")
	private int pageSize = 100;
	@ApiModelProperty(value = "orderBy", position = 3, example = "NUMBER")
	private String orderBy = "NUMBER";
}

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
public class Pagination {

	@ApiModelProperty(value = "last", position = 1)
	private boolean last;

	@ApiModelProperty(value = "nextPage", position = 2)
	private int nextPage;

	@ApiModelProperty(value = "totalCount", position = 3)
	private long totalCount;
}

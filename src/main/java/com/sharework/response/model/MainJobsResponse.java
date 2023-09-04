package com.sharework.response.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainJobsResponse {

	@ApiModelProperty(value = "id", position = 1)
	private long id;

	@ApiModelProperty(value = "coordinate", position = 2)
	private Coordinate coordinate;
}

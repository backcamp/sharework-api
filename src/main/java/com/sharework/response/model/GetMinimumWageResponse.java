package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class GetMinimumWageResponse {

	public GetMinimumWageResponse(GetMinimumWagePayload payload, BasicMeta meta) {
		this.meta = meta;
		this.payload = payload;
	}

	@ApiModelProperty(value = "payload", position = 1)
	private GetMinimumWagePayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	private BasicMeta meta;

}

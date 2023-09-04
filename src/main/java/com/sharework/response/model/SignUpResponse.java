package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class SignUpResponse {
	public SignUpResponse(BasicMeta meta, SignUpPayload payload) {
		this.meta = meta;
		this.payload = payload;
	}

	@ApiModelProperty(value = "payload", position = 2)
	private SignUpPayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	private BasicMeta meta;
}

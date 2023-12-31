package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class ImgResponse {
	public ImgResponse(ImgPayload payload, BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 2)
	private ImgPayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	private BasicMeta meta;
}

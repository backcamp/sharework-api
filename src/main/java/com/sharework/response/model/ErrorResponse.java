package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.sms.SmsPayload;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.BasicAuth;

@ApiModel
@Getter
@Setter
public class ErrorResponse{

	public ErrorResponse(BasicMeta meta){
		this.meta = meta;
	}

	@ApiModelProperty(value = "meta", position = 1)
	private BasicMeta meta;
}

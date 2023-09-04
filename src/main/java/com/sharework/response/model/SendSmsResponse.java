package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.sms.SmsPayload;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class SendSmsResponse {


	public SendSmsResponse(SmsPayload payload,BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 1)
	private SmsPayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	private BasicMeta meta;

}

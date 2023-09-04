package com.sharework.response.model.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SmsPayload {

	@ApiModelProperty(value = "sms_auth", position = 1)
	private SendSms smsAuth;
}

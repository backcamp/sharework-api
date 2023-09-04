package com.sharework.response.model;

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
public class VerifiedPayload {
	@ApiModelProperty(value = "accessToken", position = 1)
	private String accessToken;
	@ApiModelProperty(value = "refreshToken", position = 2)
	private String refreshToken;
	@ApiModelProperty(value = "userType", position = 3)
	private String userType;
}

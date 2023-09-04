package com.sharework.response.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpPayload {
	@ApiModelProperty(value = "access-token", position = 1)
	private String accessToken;
	@ApiModelProperty(value = "refresh-token", position = 2)
	private String refreshToken;

}

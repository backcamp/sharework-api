package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginObj {
	private Boolean flag;

	private String accessToken;

	private String refreshToken;

	private String userType;
}

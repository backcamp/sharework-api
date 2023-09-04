package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignInRequestPw {
	@ApiModelProperty(value = "phoneNumber", position = 3, example = "01012345678", required = true)
	@NotNull
	private String phoneNumber;
	@ApiModelProperty(value = "password", position = 6, required = true, example = "password")
	@NotNull
	private String password;
}

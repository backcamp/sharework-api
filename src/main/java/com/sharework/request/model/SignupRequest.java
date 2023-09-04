package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Valid
public class SignupRequest {

	@ApiModelProperty(value = "email", position = 1, example = "sharework@sharework.com", required = true)
	@Email
	@NotNull
	private String email;

	@ApiModelProperty(value = "name", position = 2, example = "김쉐어", required = true)
	@NotNull
	@Pattern(regexp = "^[가-힣]+$")
	private String name;

	@ApiModelProperty(value = "phoneNumber", position = 3, example = "01012345678", required = true)
	@NotNull
	@Pattern(regexp = "^[0-9]*$")
	@Size(max = 11, min = 9)
	private String phoneNumber;

	@ApiModelProperty(value = "residentnumberFront", position = 4, required = true, example = "앞 번호 6자리")
	@NotNull
	@Pattern(regexp = "^[0-9]*$")
	@Size(min = 6, max = 6)
	private String residentNumberFront;

	@ApiModelProperty(value = "residentnumberRear", position = 5, required = true, example = "뒷 번호 앞자리1개")
	@NotNull
	@Pattern(regexp = "^[0-9]*$")
	@Size(min = 1, max = 1)
	private String residentNumberRear;

	// uid를 넣어야한다. 필수값
}

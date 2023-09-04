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
@Valid
public class SignupRequestPw {

    @ApiModelProperty(value = "email", position = 1, example = "sharework@sharework.com", required = true)
    @Email
    @NotNull
    private String email;

    @ApiModelProperty(value = "name", position = 2, example = "8~20글자 사이로 영어한글숫자가능", required = true)
    @NotNull
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$")
    @Size(min = 8, max = 20, message = "닉네임을 8~20자 사이로 입력해주세요.")
    private String name;

    @ApiModelProperty(value = "phoneNumber", position = 3, example = "01012345678", required = true)
    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Size(max = 11, min = 9)
    private String phoneNumber;

    @ApiModelProperty(value = "residentnumberFront", position = 4, required = true, example = "앞 번호 6자리")
//    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 6, max = 6)
    private String residentNumberFront;

    @ApiModelProperty(value = "residentnumberRear", position = 5, required = true, example = "뒷 번호 앞자리1개")
//    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 1, max = 1)
    private String residentNumberRear;

    @ApiModelProperty(value = "password", position = 6, required = true, example = "password")
    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[@$!%*#?&])[a-zA-Z\\d@$!%*#?&]{8,20}$")
    @Size(min = 8, max = 20)
    private String password;

    // uid를 넣어야한다. 필수값

}

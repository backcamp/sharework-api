package com.sharework.controller;

import com.sharework.request.model.SignInRequestPw;
import com.sharework.request.model.SignupRequestPw;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SignUpResponse;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.VerifiedResponse;
import com.sharework.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/registration")
public class RegistrationController {
    @Autowired
    UserService userService;

//	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SignUpResponse.class),
//			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
//	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
//	@ApiOperation(httpMethod = "POST", value = "회원정보를 받아 회원가입 진행(access-token,refresh-token 발행)", notes = "insert user infomation")
//	public ResponseEntity signUp(@Valid @RequestBody(required = true) SignupRequest request,
//			BindingResult bindingResult) {
//		return userService.signUp(request, bindingResult);
//	}

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "POST", value = "회원정보를 받아 회원가입 진행(access-token,refresh-token 발행)", notes = "insert user infomation")
    public ResponseEntity signUp(@Valid @RequestBody(required = true) SignupRequestPw request,
                                 BindingResult bindingResult) {
        return userService.signUpPw(request, bindingResult);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SignUpResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "POST", value = "핸드폰 번호와 비밀번호받아 로그인 진행", notes = "Login")
    public ResponseEntity login(@Valid @RequestBody(required = true) SignInRequestPw request) {
        return userService.login(request);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(value = "/withDrawal",produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "POST", value = "토큰받아 회원탈퇴", notes = "WithDrawal")
    public ResponseEntity withDrawal(@RequestHeader("access-token") String accessToken,@RequestHeader("refresh-token") String refreshToken) {
        return userService.withDrawal(accessToken,refreshToken);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "/nickname/check",produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "닉네임을 입력받아 중복체크")
    public ResponseEntity checkNickname(String nickname) {
        return userService.checkNickname(nickname);
    }

}

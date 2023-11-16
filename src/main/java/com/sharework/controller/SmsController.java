package com.sharework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sharework.response.model.SendSmsResponse;
import com.sharework.response.model.VerifiedResponse;
import com.sharework.response.model.ErrorResponse;
import com.sharework.service.SmsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Map;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping(path = "/api/v3/sms-auth")
public class SmsController { // FIXME - SmsAuthController로 변경하는 게 좋을듯.

	@Autowired
	SmsService smsService;

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SendSmsResponse.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@PostMapping(value = "send-sms", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "POST", value = "전화번호를 받아 3분짜리 jwt토큰 반환", notes = "insert user phone_number")
	public ResponseEntity<SendSmsResponse> sendSms(@RequestBody @ApiParam(value = "Map 형식으로 핸드폰번호 전송", required = true, example = "{\r\n"
			+ "\"receiver\" : \"핸드폰번호\"\r\n" + "}") Map<String, String> params) {
		SendSmsResponse response = smsService.sendSms(params);
		return ResponseEntity.ok(response);
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = VerifiedResponse.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@PostMapping(value = "verified", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "POST", value = "인증번호 검증 API", notes = "insert user verified_number and JWT token")
	public ResponseEntity<VerifiedResponse> verifiedNumber(
			@RequestBody @ApiParam(value = "Map 형식으로 JWT 토큰 및 인증번호 요청", required = true, example = "{\r\n"
					+ "\"token\" : \"JWT토큰\"," + "\r\n" + "\"verifiedNumber\" : \"인증번호\"\r\n"
					+ "}") Map<String, String> params) {
		VerifiedResponse response = smsService.verifiedNumber(params);
		return ResponseEntity.ok(response);
	}
}

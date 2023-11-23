package com.sharework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SignUpResponse;
import com.sharework.service.JwtService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping(path = "/api/v3/jwt")
public class JwtController {

	@Autowired
	JwtService jwtService;

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SignUpResponse.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "POST", value = "jwt만료 시 jwt 재발급", notes = "Reissuance of jwt when jwt expires")
	public ResponseEntity<SignUpResponse> updateJwtToken(@RequestHeader("access-token") String accessToken,@RequestHeader("refresh-token") String refreshToken) {
		SignUpResponse response = jwtService.updateJwtToken(accessToken,refreshToken);
		return ResponseEntity.ok(response);
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SignUpResponse.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@PutMapping(value = "/{type}",produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "PUT", value = "유저정보 변경 후 jwt토큰 재발급", notes = "Reissuance of jwt token after changing user information")
	public ResponseEntity<SignUpResponse> updateUserType(@RequestHeader("access-token") String accessToken,@RequestHeader("refresh-token") String refreshToken, @PathVariable String type) {
		SignUpResponse response = jwtService.updateUserType(accessToken,refreshToken, type);
		return ResponseEntity.ok(response);
	}
}

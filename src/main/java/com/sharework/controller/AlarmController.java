package com.sharework.controller;

import com.sharework.request.model.AlarmRequest;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SuccessResponse;
import com.sharework.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @ApiResponses({
        @ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
        @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(value = "/registration", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "알람 사용자 추가 또는 갱신")
    public ResponseEntity<SuccessResponse> register(String fcmToken, @RequestHeader("access-token") String accessToken) {
        SuccessResponse response = alarmService.register(fcmToken, accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
        @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PatchMapping(value = "/send", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "알람 전송")
    public ResponseEntity<SuccessResponse> send(@RequestBody AlarmRequest alarmRequest) {
        SuccessResponse response = alarmService.sendAlarm(alarmRequest);
        return ResponseEntity.ok(response);
    }
}

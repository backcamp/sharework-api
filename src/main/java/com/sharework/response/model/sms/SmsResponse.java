package com.sharework.response.model.sms;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class SmsResponse {
	private String requestId;
	private LocalDateTime requestTime;
	private String statusCode;
	private String statusName;
}
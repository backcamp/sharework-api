package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmRequest {

    @ApiModelProperty(value = "알람 대상 FCM 토큰")
    private String targetFCMToken;

    @ApiModelProperty(value = "알람 제목", example = "구직신청")
    private String title;

    @ApiModelProperty(value = "알람 본문", example = "'[함정훈]' 님으로부터 구직 신청이 들어왔습니다.")
    private String body;

    @ApiModelProperty(value = "알람 스킴")
    private String scheme;
}

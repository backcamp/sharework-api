package com.sharework.common;

public enum JobTypeEnum {
    OPEN,  // 공고 올림
    CLOSED, // 채택 완료
    STARTED,  // 일 시작됨
    FAILED,  // 일 시작한 application이 없음
    COMPLETED,  // 일 완료
    COMPLETED_REVIEWED;
}

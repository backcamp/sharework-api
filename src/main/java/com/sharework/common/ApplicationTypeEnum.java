package com.sharework.common;

public enum ApplicationTypeEnum {
    APPLIED, // 지원함
    HIRED, // 채택됨
    HIRED_REQUEST,  // 알바시작 요청함
    HIRED_APPROVED,  // 알바시작 승인됨 (알바중)
    CANCELED,  // 지원 취소함
    REJECTED,  // 채택 됐지만 업주로부터 채택 취소당함
    FAILED,  // 채택 실패됨
    NO_SHOW,  // 채택 됐지만 일 시작 안함
    COMPLETED, // 알바완료
    COMPLETED_REVIEWED;//댓글작성완료
}
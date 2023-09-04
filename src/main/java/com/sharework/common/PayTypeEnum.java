package com.sharework.common;

public enum PayTypeEnum {
    TIEM_WAGE("시급"), // 지원함
    DAILY_WAGE("일급");

    final String getName;

    PayTypeEnum(String name) {
        getName = name;
    }
}
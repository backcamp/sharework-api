package com.sharework.response.model.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GroupStatus {
    private String name;
    private int count;
}

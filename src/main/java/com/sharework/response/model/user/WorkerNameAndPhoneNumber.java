package com.sharework.response.model.user;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
@Builder
public class WorkerNameAndPhoneNumber {
    private String name;
    private String userType;
    private String phoneNumber;
    private String userProfileImg;
}

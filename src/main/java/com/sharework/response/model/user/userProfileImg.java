package com.sharework.response.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@ApiModel
public class userProfileImg {
    @ApiModelProperty(value = "profileImg", position = 1)
    private String profileImg;
}

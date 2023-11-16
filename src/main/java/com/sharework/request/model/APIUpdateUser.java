package com.sharework.request.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class APIUpdateUser {

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "comment")
    private String comment;

}

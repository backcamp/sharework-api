package com.sharework.response.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {

    @ApiModelProperty(value = "lat", position = 3)
    private double latitude;

    @ApiModelProperty(value = "lng", position = 2)
    private double longitude;
}

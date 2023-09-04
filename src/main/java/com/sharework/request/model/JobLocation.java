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
public class JobLocation {
	@ApiModelProperty(example = "37.9", value = "북동쪽 위도")
	private Double northeastLat;
	@ApiModelProperty(example = "126.9", value = "북동쪽 경도")
	private Double northeastLng;
	@ApiModelProperty(example = "37.3", value = "남서쪽 위도")
	private Double southwestLat;
	@ApiModelProperty(example = "126.4", value = "남서쪽 경도")
	private Double southwestLng;
}

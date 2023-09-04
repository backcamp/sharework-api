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
public class JobDetail {
	@ApiModelProperty(value = "클러스터에해당하는 jobId 배열", required = true)
	private long[] jobIds;
	@ApiModelProperty(example = "0", value = "페이징 처리 쪽 숫자  (0부터 시작)", required = true)
	private int page;
	@ApiModelProperty(example = "5", value = "한번 페이징 처리의 최대 리스트 수 (Default : 5)")
	private int pageSize = 5;
	@ApiModelProperty(example = "37.9", value = "유저 위도", required = true)
	private double userLat;
	@ApiModelProperty(example = "126.9", value = "유저 경도", required = true)
	private double userLng;

}

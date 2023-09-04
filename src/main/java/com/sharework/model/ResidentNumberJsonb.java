package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ResidentNumberJsonb {
	@ApiModelProperty(example = "주민 앞 6자리", required = true)
	@JsonProperty("front")
	private String front;

	@ApiModelProperty(example = "주민 뒷 1자리", required = true)
	@JsonProperty("rear")
	private String rear;

}

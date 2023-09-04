package com.sharework.response.model.application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.sharework.model.Application;

@ApiModel
@Getter
@Setter
@AllArgsConstructor
public class ApplicationPayload {
	@ApiModelProperty(value = "applications", position = 1)
	private List<Application> applications;

}



package com.sharework.request.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
@ToString

public class RegisterReview {
	@ApiModelProperty(value = "jobId", position = 1, example = "154",required = true)
	private long jobId;

	@ApiModelProperty(value = "applicationId", position = 1, example = "80")
	private long applicationId;

	@ApiModelProperty(value = "baseReviewIds", position = 2, required = true)
	@NotNull
	private long baseReviewId[];

	@ApiModelProperty(value = "comment", position = 3, example = "정말 잘 대해주셨어요.", required = true)
	@NotNull
	private String comment;

	@ApiModelProperty(value = "starRating", position = 4, example = "4.5", required = true)
	@NotNull
	private double startRating;
}

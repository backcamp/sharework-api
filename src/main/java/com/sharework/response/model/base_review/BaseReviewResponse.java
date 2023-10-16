package com.sharework.response.model.base_review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseReviewResponse {
	public BaseReviewResponse(BaseReviewPayload payload, BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 1)
	public BaseReviewPayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	public BasicMeta meta;

	public static class BaseReviewPayload {
		@ApiModelProperty(value = "reviews")
		public List<BaseReviewDto> reviews;

		public BaseReviewPayload(List<BaseReviewDto> reviews) {
			this.reviews = reviews;
		}
	}

	public static class BaseReviewDto {
		@Column(name = "id")
		public long id;

		@Column(name = "description")
		public String description;

		public BaseReviewDto(long id, String description) {
			this.id = id;
			this.description = description;
		}
	}
}




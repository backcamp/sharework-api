package com.sharework.response.model.base_review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.Pagination;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIBaseReview {
	public APIBaseReview(Payload payload, BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 1)
	public Payload payload;

	@ApiModelProperty(value = "meta", position = 2)
	public BasicMeta meta;

	public static class Payload {
		@ApiModelProperty(value = "reviews")
		public List<BaseReview> reviews;

		public Payload(List<BaseReview> reviews) {
			this.reviews = reviews;
		}
	}

	public static class BaseReview {
		@Column(name = "id")
		public long id;

		@Column(name = "description")
		public String description;

		public BaseReview(long id, String description) {
			this.id = id;
			this.description = description;
		}
	}
}




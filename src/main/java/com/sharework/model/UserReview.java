package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "user_review")

public class UserReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "base_review_id")
	private long baseReviewId;

	@NotNull
	@Column(name = "user_id")
	private long userId;

	@Column(name = "count")
	private long count;

	@Column(name = "user_type")
	private String userType;

	@Builder
	public UserReview(long baseReviewId, long userId, long count, String userType) {
		this.baseReviewId = baseReviewId;
		this.userId = userId;
		this.count = count;
		this.userType = userType;
	}
}

package com.sharework.model.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "base_review")
@Data
public class BaseReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "contents")
	private String contents;

	@Column(name = "user_type")
	private String userType;

	@Builder
	public BaseReview(String contents, String userType) {
		this.contents = contents;
		this.userType = userType;
	}
}

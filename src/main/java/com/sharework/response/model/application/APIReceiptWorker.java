package com.sharework.response.model.application;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class APIReceiptWorker {
	public APIReceiptWorker(Payload payload, BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 1)
	public Payload payload;

	@ApiModelProperty(value = "meta", position = 2)
	public BasicMeta meta;

	public class Payload {
		@ApiModelProperty(value = "application")
		public Application application;

		public Payload(Application application) {
			this.application = application;
		}
	}

	public static class Job {
		@Id
		@Column(name = "id")
		public long id;

		@Column(name = "title")
		public String title;

		@Column(name = "startAt")
		public LocalDateTime startAt;

		@Column(name = "endAt")
		public LocalDateTime endAt;

		@Column(name = "totalPayment")
		public Integer totalPayment;

		@ApiModelProperty(value = "jobTags")
		public List<JobTag> jobTags;

		@ApiModelProperty(value = "user")
		public User user;

		public Job(long id, String title, LocalDateTime startAt, LocalDateTime endAt, int totalPayment, List<JobTag> jobTags,
				   User user) {
			this.id = id;
			this.title = title;
			this.startAt = startAt;
			this.endAt = endAt;
			this.totalPayment = totalPayment;
			this.jobTags = jobTags;
			this.user = user;
		}
	}

	public static class JobTag {
		@Column(name = "contents")
		public String contents;

		public JobTag(String contents) {
			this.contents = contents;
		}
	}

	public static class Application {

		@Id
		@Column(name = "id")
		public long id;

		@Column(name = "isReview")
		public Boolean isReview;

		@ApiModelProperty(value = "job")
		public Job job;

		public Application(long id, Boolean isReview, Job job) {
			this.id = id;
			this.isReview = isReview;
			this.job = job;
		}
	}

	public static class User {
		@Column(name = "name")
		public long name;

		@Column(name = "profileImg")
		public String profileImg;

		public User(long name, String profileImg) {
			this.name = name;
			this.profileImg = profileImg;
		}
	}
}




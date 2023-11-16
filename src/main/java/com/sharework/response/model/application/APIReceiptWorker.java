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
	public APIReceiptWorker(RwPayload payload, BasicMeta meta) {
		this.payload = payload;
		this.meta = meta;
	}

	@ApiModelProperty(value = "payload", position = 1)
	public RwPayload payload;

	@ApiModelProperty(value = "meta", position = 2)
	public BasicMeta meta;

	public static class RwPayload {
		public RwApplication application;

		public RwPayload(RwApplication application) {
			this.application = application;
		}
	}

	public static class RwJob {

		public long id;

		public String title;

		public LocalDateTime startAt;

		public LocalDateTime endAt;

		public Integer totalPayment;

		public List<RwJobTag> jobTags;

		public RwGiver giver;

		public RwJob(long id, String title, LocalDateTime startAt, LocalDateTime endAt, int totalPayment, List<RwJobTag> jobTags,
					 RwGiver giver) {
			this.id = id;
			this.title = title;
			this.startAt = startAt;
			this.endAt = endAt;
			this.totalPayment = totalPayment;
			this.jobTags = jobTags;
			this.giver = giver;
		}
	}

	public static class RwJobTag {
		public String contents;

		public RwJobTag(String contents) {
			this.contents = contents;
		}
	}

	public static class RwApplication {

		public long id;

		public Boolean isReview;

		public RwJob job;

		public RwApplication(long id, Boolean isReview, RwJob job) {
			this.id = id;
			this.isReview = isReview;
			this.job = job;
		}
	}

	public static class RwGiver {
		@Column(name = "name")
		public long name;

		@Column(name = "profileImg")
		public String profileImg;

		public RwGiver(long name, String profileImg) {
			this.name = name;
			this.profileImg = profileImg;
		}
	}
}




package com.sharework.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cancel_reason")
@NoArgsConstructor
public class cancelReason {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "application_id")
	private long applicationId;

	@NotNull
	@Column(name = "job_id")
	private long jobId;

	@NotNull
	@Column(name = "user_id")
	private long userId;

	@Column(name = "reason")
	private String reason;

	@Builder
	public cancelReason(long jobId, long userId, long applicationId, String reason) {
		this.applicationId = applicationId;
		this.jobId = jobId;
		this.userId = userId;
		this.reason = reason;
	}
}

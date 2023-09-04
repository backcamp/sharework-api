package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "report")

public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "job_id")
	private long jobId;

	@NotNull
	@Column(name = "user_id")
	private long userId;

	@NotNull
	@Column(name = "base_report_id")
	private long baseReportId;

	@Builder
	public Report(long jobId, long userId, long baseReportId) {
		this.jobId = jobId;
		this.userId = userId;
		this.baseReportId = baseReportId;
	}
}

package com.sharework.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "job_benefit")
@NoArgsConstructor
public class JobBenefit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "job_id")
	private long jobId;

	@NotNull
	@Column(name = "base_benefit_id")
	private long baseBenefitId;

	@Builder
	public JobBenefit(long jobId, long baseBenefitId) {
		this.jobId = jobId;
		this.baseBenefitId = baseBenefitId;
	}
}

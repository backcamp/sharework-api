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
@Table(name = "job_checklist")
@NoArgsConstructor
public class JobCheckList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "job_id")
	private long jobId;

	@NotNull
	@Column(name = "checklist_id")
	private long checkListId;

	@NotNull
	@Column(name = "contents")
	private String contents;


	@Builder
	public JobCheckList(long jobId, long checkListId,String contents) {

		this.jobId = jobId;
		this.checkListId = checkListId;
		this.contents = contents;
	}
}

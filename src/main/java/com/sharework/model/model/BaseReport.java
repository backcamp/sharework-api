package com.sharework.model.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "base_report")
public class BaseReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "contents")
	private String contents;

	@Column(name = "type")
	private String type;

	@Builder
	public BaseReport(String contents, String type) {
		this.contents = contents;
		this.type = type;
	}
}

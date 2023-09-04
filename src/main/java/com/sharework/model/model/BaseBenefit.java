package com.sharework.model.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "base_benefit")
public class BaseBenefit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "contents")
	private String contents;

	@Builder
	public BaseBenefit(String contents) {
		this.contents = contents;
	}
}

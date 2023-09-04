package com.sharework.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "application")
@Data
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@ApiModelProperty(value = "id", position = 1, hidden = true)
	private long id;

	@NotNull
	@Column(name = "job_id")
	private long jobId;

	@Column(name = "user_id")
	@NotNull
	@JsonIgnore
	@ApiModelProperty(value = "userId", position = 3, example = "20", hidden = true)
	private long userId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "startAt", position = 4, example = "2015-04-13 11:00:00", required = true, hidden = true)
	@Column(name = "start_at")
	private LocalDateTime startAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "endAt", position = 5, example = "2017-01-12 18:00:00", required = true, hidden = true)
	@Column(name = "end_at")
	private LocalDateTime endAt;

	@ApiModelProperty(value = "lat", position = 6, example = "37.9", required = true)
	@NotNull
	@Column(name = "lat")
	private double lat;

	@ApiModelProperty(value = "lng", position = 7, example = "126.9", required = true)
	@NotNull
	@Column(name = "lng")
	private double lng;

	@Column(name = "status")
	@ApiModelProperty(value = "status", position = 8, required = true, hidden = true)
	private String status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public Application(long id, long userId, long jobId, LocalDateTime startAt, LocalDateTime endAt, double lat,
			double lng, String status) {
		LocalDateTime now = LocalDateTime.now();
		this.id = id;
		this.userId = userId;
		this.jobId = jobId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.createdAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());
		this.lat = lat;
		this.lng = lng;
		this.status = status;
	}
}

package com.sharework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "location_favorite")

public class LocationFavorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	@Column(name = "id")
	private long id;

	@NotNull
	@Column(name = "user_id")
	private long userId;

	@NotNull
	@Column(name = "location_name")
	private String locationName;

	@NotNull
	@Column(name = "lat")
	private double lat;

	@NotNull
	@Column(name = "lng")
	private double lng;

	@Builder
	public LocationFavorite(long userId, String locationName, double lat, double lng) {
		this.userId = userId;
		this.locationName = locationName;
		this.lat = lat;
		this.lng = lng;
	}
}

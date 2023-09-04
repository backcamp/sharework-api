package com.sharework.request.model;


import com.sharework.response.model.Coordinate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationFavoriteRequest {

	public LocationFavoriteRequest(	LocalInfomation[] localInfomations){
		this.localInfomations = localInfomations;
	}

	public LocalInfomation[] localInfomations;

	 static public class LocalInfomation {

		public String title;

		public Coordinate coordinate;

		public LocalInfomation(Coordinate coordinate,String title){
			this.title = title;
			this.coordinate = coordinate;
		}
	}
}

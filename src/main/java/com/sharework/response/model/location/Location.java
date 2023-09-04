package com.sharework.response.model.location;

import com.sharework.response.model.Coordinate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Location {
    @ApiModelProperty(value = "id")
    private long id;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "coordinate")
    private Coordinate coordinate;

    @Builder
    public Location(long id, String title, Coordinate coordinate){
        this.id = id;
        this.title = title;
        this.coordinate = coordinate;
    }
}

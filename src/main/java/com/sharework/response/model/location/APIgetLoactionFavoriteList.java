package com.sharework.response.model.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIgetLoactionFavoriteList {

    public APIgetLoactionFavoriteList(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class Payload {
        @ApiModelProperty(value = "favoriteLocations")
        public List<Location> favoriteLocations;

        public Payload(List<Location> favoriteLocations) {
           this.favoriteLocations = favoriteLocations;
        }
    }
}




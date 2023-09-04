package com.sharework.response.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class Giver {
    @ApiModelProperty(value = "id")
    private long id;
    @ApiModelProperty(value = "name")
    private String name;
    @ApiModelProperty(value = "profileImg")
    public String profileImg;

    @Builder
    public Giver(long id, String name, String profileImg) {
        this.id = id;
        this.name = name;
        this.profileImg = profileImg;
    }
}

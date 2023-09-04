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
public class Profile {
    @ApiModelProperty(value = "user")
    private Giver user;
    @ApiModelProperty(value = "jobCount")
    private int jobCount;
    @ApiModelProperty(value = "rate")
    public double rate;
    @ApiModelProperty(value = "description")
    public String description;

    @Builder
    public Profile(Giver user, int jobCount, double rate, String description) {
        this.user = user;
        this.jobCount = jobCount;
        this.rate = rate;
        this.description = description;
    }
}

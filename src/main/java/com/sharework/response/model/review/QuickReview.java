package com.sharework.response.model.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
public class QuickReview {

    @ApiModelProperty(value = "id", position = 1)
    private long id;

    @ApiModelProperty(value = "description", position = 2)
    public String description;


    @Builder
    public QuickReview(long id, String description) {
        this.id = id;
        this.description = description;
    }
}

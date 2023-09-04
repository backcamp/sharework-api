package com.sharework.response.model.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
public class QuickReviewRank {

    @ApiModelProperty(value = "quickReview", position = 1)
    private QuickReview quickReview;

    @ApiModelProperty(value = "count", position = 2)
    public long count;


    @Builder
    public QuickReviewRank(QuickReview quickReview, long count) {
        this.quickReview = quickReview;
        this.count = count;
    }
}

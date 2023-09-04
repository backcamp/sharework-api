package com.sharework.response.model.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGetReview {
    public APIGetReview(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class Payload {
        @ApiModelProperty(value = "quickReview")
        public List<QuickReviewRank> quickReviewRanks;

        @ApiModelProperty(value = "detailReview")
        public List<DetailReview> detailReviews;

        public Payload(List<QuickReviewRank> quickReviewRanks, List<DetailReview> detailReviews) {
            this.quickReviewRanks = quickReviewRanks;
            this.detailReviews = detailReviews;
        }
    }
}




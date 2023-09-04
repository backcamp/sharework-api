package com.sharework.response.model.review;

import com.sharework.response.model.job.JobTagList;
import com.sharework.response.model.user.Giver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
public class DetailReview {

    @ApiModelProperty(value = "id", position = 1)
    private long id;

    @ApiModelProperty(value = "user", position = 2)
    private Giver user;

    @ApiModelProperty(value = "user", position = 2)
    private String comment;

    @ApiModelProperty(value = "createAt", position = 3)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "rate", position = 4)
    private double rate;

    @ApiModelProperty(value = "tags", position = 5)
    private List<JobTagList> tags;

    @Builder
    public DetailReview(long id, Giver user, String comment, LocalDateTime createdAt, double rate, List<JobTagList> tags) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.createdAt = createdAt;
        this.rate = rate;
        this.tags = tags;
    }
}

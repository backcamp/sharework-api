package com.sharework.response.model.user;

import com.sharework.response.model.tag.TagRank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @ApiModelProperty(value = "user")
    private Giver user;
    @ApiModelProperty(value = "jobCount")
    private int jobCount;
    @ApiModelProperty(value = "rate")
    private double rate;
    @ApiModelProperty(value = "description")
    private String description;
    @ApiModelProperty(value = "tagRanks")
    private List<TagRank> tagRanks;

    @Builder
    public Profile(Giver user, int jobCount, double rate, String description, List<TagRank> tagRanks) {
        this.user = user;
        this.jobCount = jobCount;
        this.rate = rate;
        this.description = description;
        this.tagRanks = tagRanks;
    }
}

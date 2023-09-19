package com.sharework.response.model.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGetTagRank {
    public APIGetTagRank(APIGetTagRankPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public APIGetTagRankPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    public static class APIGetTagRankPayload {
        @ApiModelProperty(value = "profile")
        public List<TagRank> tagRanks;

        public APIGetTagRankPayload(List<TagRank> tagRanks) {
            this.tagRanks = tagRanks;
        }
    }
}

package com.sharework.response.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGetUser {
    public APIGetUser(APIGetUserPayload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public APIGetUserPayload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    @Data
    @NoArgsConstructor
    public static class APIGetUserPayload {
        @ApiModelProperty(value = "profile")
        public Profile profile;

        public APIGetUserPayload(Profile profile) {
            this.profile = profile;
        }
    }
}

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
    public APIGetUser(Payload payload, BasicMeta meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @ApiModelProperty(value = "payload", position = 1)
    public Payload payload;

    @ApiModelProperty(value = "meta", position = 2)
    public BasicMeta meta;

    @Data
    @NoArgsConstructor
    public static class Payload {
        @ApiModelProperty(value = "profile")
        public Profile profile;

        public Payload(Profile profile) {
            this.profile = profile;
        }
    }
}

package com.sharework.response.model;

import com.sharework.response.model.meta.BasicMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Data
@NoArgsConstructor
public class SuccessResponse {


    public SuccessResponse(BasicMeta meta) {
        this.meta = meta;
    }

    @ApiModelProperty(value = "meta", position = 1)
    private BasicMeta meta;

}

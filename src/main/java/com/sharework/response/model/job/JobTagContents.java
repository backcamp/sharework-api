package com.sharework.response.model.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel
public class JobTagContents {
    @ApiModelProperty(value = "contents")
    private String contents;

    public JobTagContents(String contents){
        this.contents = contents;
    }
}

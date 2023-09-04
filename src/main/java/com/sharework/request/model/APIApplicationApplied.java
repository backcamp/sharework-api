package com.sharework.request.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Data
public class APIApplicationApplied {
    @ApiModelProperty(value = "체크리스트 아이디 리스트")
    private List<Integer> applicationChecklistIds;

    @ApiModelProperty(value = "job id")
    private long jobId;

    @ApiModelProperty(example = "37.3", value = "유저 위도")
    private Double lat;

    @ApiModelProperty(example = "126.9", value = "유저 경도")
    private Double lng;

}

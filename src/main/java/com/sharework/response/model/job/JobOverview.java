package com.sharework.response.model.job;

import com.sharework.response.model.Coordinate;
import com.sharework.response.model.user.Giver;
import com.sharework.response.model.user.userProfileImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class JobOverview {
    @ApiModelProperty(value = "id")
    private long id;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "coordinate")
    private Coordinate coordinate;

    @ApiModelProperty(value = "giver")
    private Giver giver;

    @ApiModelProperty(value = "starAt")
    private LocalDateTime startAt;

    @ApiModelProperty(value = "endAt")
    private LocalDateTime endAt;

    @ApiModelProperty(value = "pay")
    private int pay;

    @ApiModelProperty(value = "payType")
    private String payType;

    @ApiModelProperty(value = "totalPay")
    private int totalPay;

    @ApiModelProperty(value = "tags")
    private List<JobTagList> tags;

    @Builder
    public JobOverview(long id, String title, Coordinate coordinate, Giver giver, LocalDateTime startAt, LocalDateTime endAt, String payType, int pay, int totalPay,
                       List<JobTagList> tags) {
        this.id = id;
        this.title = title;
        this.coordinate = coordinate;
        this.giver = giver;
        this.startAt = startAt;
        this.endAt = endAt;
        this.payType = payType;
        this.pay = pay;
        this.totalPay = totalPay;
        this.tags = tags;
    }
}

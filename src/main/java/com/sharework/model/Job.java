package com.sharework.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "job")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@ApiModelProperty(hidden = true)
    @ApiModelProperty(value = "id", position = 1, example = "20", required = true)
    @Column(name = "id")
    private long id;

    @ApiModelProperty(hidden = true)
    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty(value = "title", position = 2, example = "서울 강남역 1인 구합니다.", required = true)
    @NotNull
    @Column(name = "title")
    private String title;

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "startAt", position = 3, example = "2015-04-13 09:00:00", required = true)
    @NotNull
    @Column(name = "start_at")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "endAt", position = 4, example = "2017-01-12 18:00:00", required = true)
    @NotNull
    @Column(name = "end_at")
    private LocalDateTime endAt;

    @ApiModelProperty(value = "lat", position = 5, example = "37.9", required = true)
    @NotNull
    @Column(name = "lat")
    private double lat;

    @ApiModelProperty(value = "lng", position = 6, example = "126.9", required = true)
    @NotNull
    @Column(name = "lng")
    private double lng;

    @ApiModelProperty(value = "address", position = 7, example = "서울 강남구 삼성동 47-26", required = true)
    @NotNull
    @Column(name = "address")
    private String address;

    @ApiModelProperty(value = "addressDetail", position = 8, example = "우봉빌라201호")
    @Column(name = "address_detail")
    private String addressDetail;

    @ApiModelProperty(value = "personnel", position = 9, example = "4", required = true)
    @Column(name = "personnel")
    private long personnel;

    @ApiModelProperty(value = "payType", position = 10, example = "시급", required = true)
    @Column(name = "pay_type")
    private String payType;

    @ApiModelProperty(value = "pay", position = 11, example = "10000")
    @Column(name = "pay")
    private int pay;

    @ApiModelProperty(value = "contents", position = 12, example = "개꿀임", required = true)
    @NotNull
    @Column(name = "contents")
    private String contents;

    @ApiModelProperty(value = "status", position = 13, example = "OPEN", required = true)
    @NotNull
    @Column(name = "status")
    private String status;

    @ApiModelProperty(value = "paymentToday", example = "true", required = true)
    @NotNull
    @Column(name = "payment_today")
    private Boolean paymentToday;

    @Builder
    public Job(long id, long userId, String title, LocalDateTime startAt, LocalDateTime endAt, double lat, double lng,
               String address, String addressDetail, long personnel, String payType, int pay, String contents,
               String status, Boolean paymentToday) {
        Assert.hasText(title, "title이 없습니다.");
        Assert.hasText(address, "address가 없습니다");
        Assert.hasText(status, "status가 없습니다");
        LocalDateTime now = LocalDateTime.now();
        this.id = id;
        this.userId = userId == 0 ? null : userId;
        this.title = title;
        this.createdAt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());
        this.startAt = startAt;
        this.endAt = endAt;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.addressDetail = addressDetail;
        this.personnel = personnel;
        this.payType = payType;
        this.pay = pay;
        this.contents = contents;
        this.status = status;
        this.paymentToday = paymentToday;
    }
}

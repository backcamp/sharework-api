package com.sharework.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "user_rate")
public class UserRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_type")
    @NotNull
    @ApiModelProperty(value = "user_type", position = 5, example = "worker || giver")
    private String userType;

    @Column(name = "rate")
    @NotNull
    private double rate;

    @Column(name = "user_id")
    @NotNull
    private long userId;

    @Builder
    public UserRate(String userType, double rate, long userId) {
        Assert.hasText(userType, "userType이 없습니다.");
        this.userType = userType;
        this.rate = rate;
        this.userId = userId;
    }
}

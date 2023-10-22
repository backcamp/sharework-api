package com.sharework.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String fcmToken;

    private LocalDateTime updatedAt;

    @NotNull
    private Long userId;

    @Builder
    public UserAlarm(String fcmToken, Long userId) {
        this.fcmToken = fcmToken;
        this.updatedAt = LocalDateTime.now();
        this.userId = userId;
    }
}

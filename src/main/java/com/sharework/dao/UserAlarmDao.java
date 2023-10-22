package com.sharework.dao;

import com.sharework.model.UserAlarm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlarmDao extends JpaRepository<UserAlarm, Long> {

    Optional<UserAlarm> findByUserId(long userId);

}

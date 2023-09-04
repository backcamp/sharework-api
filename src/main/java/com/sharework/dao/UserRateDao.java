package com.sharework.dao;

import com.sharework.model.UserRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRateDao extends JpaRepository<UserRate, Long> {
    Optional<UserRate> findByUserTypeAndUserId(String userType, long userId);
}

package com.sharework.dao;

import com.sharework.model.Review;
import com.sharework.model.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReviewDao extends JpaRepository<UserReview, Long> {
    Optional<UserReview> findByUserIdAndBaseReviewIdAndUserType(long userId, long baseReviewId, String userType);

    boolean existsByUserIdAndBaseReviewIdAndUserType(long userId, long baseReviewId, String userType);

    List<UserReview> getByUserIdAndUserType(long userId,String userType);
}


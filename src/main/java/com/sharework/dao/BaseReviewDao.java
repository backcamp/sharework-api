package com.sharework.dao;


import com.sharework.model.model.BaseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseReviewDao extends JpaRepository<BaseReview, Long> {

    List<BaseReview> getByUserType(String userType);

}

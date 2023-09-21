package com.sharework.dao;

import com.sharework.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewDao extends JpaRepository<Review, Long> {

    List<Review> findByWorkerId(long workerId);

    List<Review> findByGiverId(long giverId);

    List<Review> findByWorkerIdAndReviewType(long workerId, String reviewType);

    List<Review> findByGiverIdAndReviewType(long giverId, String reviewType);

    boolean existsByWorkerIdAndJobIdAndReviewType(long WorkerId, long jobId, String reviewType);

    @Query(value = "SELECT round(CAST(avg(star_rating) as numeric), 2) FROM review WHERE review_type = 'WORKER' and giver_id =:giverId", nativeQuery = true)
    double getAvgRateGiver(@Param("giverId") long giverId);

    @Query(value = "SELECT round(CAST(avg(star_rating) as numeric), 2) FROM review WHERE review_type = 'GIVER' and worker_id =:workerId", nativeQuery = true)
    double getAvgRateWorker(@Param("workerId") long workerId);
}


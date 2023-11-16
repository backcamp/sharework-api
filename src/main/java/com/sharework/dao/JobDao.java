package com.sharework.dao;

import com.sharework.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobDao extends JpaRepository<Job, Long> {

    @Query(value = "SELECT * FROM job WHERE lat BETWEEN :s.Lat AND :n.Lat AND lng BETWEEN  :s.Lng AND :n.Lng AND status ='OPEN' LIMIT 800", nativeQuery = true)
    List<Job> selectJobs(@Param("s.Lat") double SouthwestLat, @Param("n.Lat") double northeastLat, @Param("s.Lng") double SouthwestLng, @Param("n.Lng") double northeastLng);

    @Query(value = "SELECT j FROM Job j WHERE j.id IN ?1")
    Page<Job> findJobsDetail(long[] JobId, Pageable pageable);

    List<Job> findJobsByUserId(long userId);

    List<Job> findTop10ByUserIdOrderByIdDesc(long userId);

    @Query(value = "select * from job where user_id = :user_id IN status = :status", nativeQuery = true)
    Page<Job> findByUserIdAndProceeding(@Param("user_id") long UserId, @Param("status") List<String> status, Pageable pageable);

    Page<Job> findByUserIdAndStatusIn(long userId, List<String> status, Pageable pageable);

    List<Job> findByUserIdAndStatusIn(long userId, List<String> status);

    @Query(value = "select * from job where user_id = :user_id", nativeQuery = true)
    Page<Job> findJobsByUserId(@Param("user_id") long userId, Pageable pageable);

    @Query(value = "select * from job where user_id = :user_id and status = :status", nativeQuery = true)
    Page<Job> getStatusJobList(@Param("user_id") long userId, @Param("status") String status, Pageable pageable);

    @Query(value = "SELECT * FROM job WHERE start_at <= now() and status IN ('OPEN','CLOSED')", nativeQuery = true)
    List<Job> getStartTimeJobs();

    @Query(value = "SELECT * FROM job WHERE end_at <= now() and status='STARTED'", nativeQuery = true)
    List<Job> getEndTimeoutStartedJobs();

    @Query(value = "SELECT * FROM job WHERE end_at <= now() and status IN ('OPEN','CLOSED')", nativeQuery = true)
    List<Job> getEndTimeoutOpenAndClosedJobs();

    List<Job> getByUserIdAndStatus(long userId, String status);

    int countByUserId(long userId);
}

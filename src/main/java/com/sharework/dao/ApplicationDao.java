package com.sharework.dao;

import com.sharework.model.Application;
import com.sharework.response.model.job.GroupStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ApplicationDao extends JpaRepository<Application, Long> {
    List<Application> findUserIdByJobId(long jobId);

    Application findFirstByJobIdOrderById(long jobId);

    Application findByJobIdAndUserId(long jobId, long userId);

    List<Application> findByJobIdAndUserIdAndStatusContaining(long jobId, long userId, String status);

    List<Application> findByJobIdAndStatusContaining(long jobId, String status);

    List<Application> findByJobIdAndStatus(long jobId, String status);

    List<Application> findByJobIdAndStatusIn(long jobId, List<String> status);

    @Query(value = "select * from application where job_id = :job_id and status = :status", nativeQuery = true)
    Page<Application> findByJobIdAndApplied(@Param("job_id") long JobId, @Param("status") String status, Pageable pageable);

    @Query(value = "SELECT status name,count(status) count FROM application WHERE job_id = :job_id GROUP BY status  having  status in('APPLIED','HIRED','HIRED_APPROVED','HIRED_REQUEST') ORDER BY COUNT(status) DESC, CASE WHEN status = 'HIRED_APPROVED' THEN 1 WHEN status = 'HIRED_REQUEST' THEN 2 WHEN status = 'HIRED' THEN 3 WHEN status = 'APPLIED' THEN 4 ELSE 5 end limit 1", nativeQuery = true)
    GroupStatus processingGroupStatus(@Param("job_id") long JobId);

    @Query(value = "SELECT 'COMPLETED' AS name, (select count(*) FROM application a WHERE job_id = :job_id and status like 'C%') as count",nativeQuery = true)
    GroupStatus completedGroupStatus(@Param("job_id") long JobId);

    Page<Application> getByUserIdAndStatusContaining(long userId, String status, Pageable pageable);

    Page<Application> getByUserIdAndStatusContainingOrderByStartAt(long userId, String status, Pageable pageable);

    @Query(value = "SELECT count(*) FROM application WHERE user_id = :user_id and status IN (:status)", nativeQuery = true)
    int countByUserIdAndStatusIn(@Param("user_id") long userId, @Param("status") List<String> status);

    List<Application> getByUserIdAndStatus(long userId, String status);

    List<Application> getByUserIdAndStatusIn(long userId, ArrayList<String> status);

    int countByUserIdAndStatus(long userId, String status);

    int countByJobIdAndStatus(long jobId, String status);

    int countByJobIdAndStatusStartingWith(long jobId, String status);

    int countByJobIdAndStatusContaining(long jobId, String status);


    @Query(value = "SELECT * FROM application WHERE status ='APPLIED' and job_id IN(SELECT id FROM job WHERE start_at <= now() and status ='CLOSED')", nativeQuery = true)
    List<Application> getStartedStatus();

    @Query(value = "SELECT * FROM application WHERE end_at <= now() and status = 'APPLIED'", nativeQuery = true)
    List<Application> getEndTimeoutAppliedApplication();

    @Query(value = "SELECT * FROM application WHERE end_at <= now() and status = 'HIRED_REQUEST'", nativeQuery = true)
    List<Application> getEndTimeoutHiredRequestApplication();

    @Query(value = "SELECT * FROM application WHERE end_at <= now() and status = 'HIRED_APPROVED'", nativeQuery = true)
    List<Application> getEndTimeoutHiredApprovedApplication();

    @Query(value = "SELECT * FROM application WHERE end_at <= now() and status = 'HIRED'", nativeQuery = true)
    List<Application> getEndTimeoutHiredApplication();

    List<Application> findByEndAtLessThanEqualAndStatusEqualsOrderByEndAtAsc(LocalDateTime endAt, String status);

    @Query(value = "SELECT sum(date_part('hour', app.end_at-app.start_at)) as hour FROM application app WHERE user_id =  :userId and job_id in(select jt.job_id from job_tag jt where jt.contents = :contents)", nativeQuery = true)
    int countByUserIdAndTagContents(@Param("userId") long userId, @Param("contents") String contents);
}

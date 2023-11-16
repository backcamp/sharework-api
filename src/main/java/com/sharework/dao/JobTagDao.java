package com.sharework.dao;


import com.sharework.response.model.job.JobTagContents;
import com.sharework.response.model.tag.JobTagRank;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharework.model.JobTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobTagDao extends JpaRepository<JobTag, Long> {
    List<JobTag> findByJobId(long jobId);

    @Query(value = "SELECT contents, count(contents) count, id FROM job_tag WHERE job_id in(:jobId) group by contents, id order by count desc,contents limit 3", nativeQuery = true)
    List<JobTagRank> findByJobIdCountContentsId(@Param("jobId") List<Long> jobId);
}

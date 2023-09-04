package com.sharework.dao;


import com.sharework.model.JobBenefit;
import com.sharework.model.JobTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobBenefitDao extends JpaRepository<JobBenefit, Long> {
    List<JobBenefit> findByJobId(long jobId);

}

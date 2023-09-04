package com.sharework.dao;

import com.sharework.model.JobBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharework.model.JobCheckList;

import java.util.List;
import java.util.Optional;

public interface JobCheckListDao extends JpaRepository<JobCheckList, Long> {

    List<JobCheckList> findByJobId(long jobId);
    Optional<JobCheckList> findByJobIdAndCheckListId(long jobId,long checkListId);
}

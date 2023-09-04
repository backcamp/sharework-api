package com.sharework.dao;

import com.sharework.model.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sharework.model.ApplicationChecklist;

import java.util.List;

public interface ApplicationChecklistDao extends JpaRepository<ApplicationChecklist, Long> {
    List<ApplicationChecklist> findByApplicationId(long applicationId);
}

package com.sharework.dao;

import java.util.List;
import java.util.Optional;

import com.sharework.model.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChecklistDao extends JpaRepository<UserChecklist, Long> {
	Optional<UserChecklist> getByUserId(long id);

	Optional<UserChecklist> findByid(long userCheckListId);
}

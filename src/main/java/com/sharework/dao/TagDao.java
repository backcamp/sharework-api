package com.sharework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sharework.model.model.BaseTag;

public interface TagDao extends JpaRepository<BaseTag, Long> {
}

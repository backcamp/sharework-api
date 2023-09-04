package com.sharework.dao;


import com.sharework.model.model.BaseBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseBenefitDao extends JpaRepository<BaseBenefit, Long> {
    Optional<BaseBenefit> findById(long id);

}

package com.sharework.dao;

import com.sharework.model.Application;
import com.sharework.model.ApplicationTotalPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ApplicationTotalPaymentDao extends JpaRepository<ApplicationTotalPayment, Long> {

    ApplicationTotalPayment getByApplicationId(long applicationId);
    List<ApplicationTotalPayment> getByJobId(long jobId);
}
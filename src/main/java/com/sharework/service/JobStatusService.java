package com.sharework.service;

import com.sharework.dao.JobDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.Job;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.JobStatusResponse;
import com.sharework.response.model.job.JobPayload;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JobStatusService {
    @Autowired
    JobDao jobDao;
    @Autowired
    TokenIdentification identification;

    public ResponseEntity getJobList(int page, int size, String token) {

        ResponseEntity response = null;
        ErrorResponse error = null;
        BasicMeta meta;

        long id = identification.getHeadertoken(token);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Job> jobList = jobDao.findJobsByUserId(id, pageRequest);

        if (jobList.isEmpty()) {
            String errorMsg = "등록된 일감이 없습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        JobPayload jobPayload = new JobPayload(jobList);
//        Pagination pagination = new Pagination(jobList.isLast(), jobList.getTotalPages());
        final JobStatusResponse result = new JobStatusResponse(jobPayload, null);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getCompletedJobList(int page, int size, String token) {
        ResponseEntity response = null;
        ErrorResponse error = null;
        BasicMeta meta;

        long id = identification.getHeadertoken(token);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Job> jobList = jobDao.getStatusJobList(id, "CLOSED", pageRequest);

        if (jobList.isEmpty()) {
            String errorMsg = "등록된 일감이 없습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        JobPayload jobPayload = new JobPayload(jobList);
//        Pagination pagination = new Pagination(jobList.isLast(), jobList.getTotalPages());

        final JobStatusResponse result = new JobStatusResponse(jobPayload, null);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
}

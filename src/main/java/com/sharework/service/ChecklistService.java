package com.sharework.service;

import com.sharework.dao.JobCheckListDao;
import com.sharework.dao.UserChecklistDao;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.JobCheckList;
import com.sharework.model.UserChecklist;
import com.sharework.request.model.CheckListName;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.UserChecklistPayload;
import com.sharework.response.model.UserChecklistResponse;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    UserChecklistDao userChecklistDao;
    @Autowired
    TokenIdentification identification;
    @Autowired
    JobCheckListDao jobCheckListDao;

    public ResponseEntity insertUserChecklist(String accessToken, CheckListName checklistName) {
        ResponseEntity response = null;
        ErrorResponse error = null;

        long id = identification.getHeadertoken(accessToken);
        BasicMeta meta = new BasicMeta();

        String[] beforeCheckList = checklistName.getCheckListName();
//        if (beforeCheckList.length == 0) {
//            String errorMsg = "체크리스트를 적어주세요.";
//            meta = new BasicMeta(true, errorMsg);
//            error = new ErrorResponse(meta);
//            response = new ResponseEntity<>(error, HttpStatus.OK);
//            return response;
//        }
        if (beforeCheckList.length > 5) {
            String errorMsg = "체크리스트 제한을 넘었습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        Optional<UserChecklist> userChecklist = userChecklistDao.getByUserId(id);
        userChecklist.ifPresentOrElse(checkList -> {
            //빈 배열일 경우 삭제로 구현.
            if (beforeCheckList.length == 0) {
                userChecklistDao.deleteById(checkList.getId());
                return;
            }
            checkList.setContents(beforeCheckList);
            userChecklistDao.save(checkList);
        }, () -> {
            if (beforeCheckList.length == 0)
                return;

            userChecklistDao.save(UserChecklist.builder().userId(id).contents(beforeCheckList).build());
        });

        meta = new BasicMeta(true, "체크리스트가 성공적으로 저장되었습니다.");
        SuccessResponse result = new SuccessResponse(meta);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public ResponseEntity getChecklist(String accessToken) {
        ResponseEntity response = null;
        ErrorResponse error = null;

        long id = identification.getHeadertoken(accessToken);

        Optional<UserChecklist> userChecklist = userChecklistDao.getByUserId(id);
        BasicMeta meta = new BasicMeta();

        if (userChecklist.isEmpty()) {
            String errorMsg = "유저가 등록한 체크리스트가 없습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }

        meta = new BasicMeta(true, "체크리스트 제공이 완료되었습니다.");
        UserChecklistPayload userChecklistPayload = new UserChecklistPayload(userChecklist.get());
        UserChecklistResponse userChecklistResponse = new UserChecklistResponse(userChecklistPayload, meta);
        response = new ResponseEntity<>(userChecklistResponse, HttpStatus.OK);
        return response;
    }

    public ResponseEntity delChecklist(String accessToken, long checklistId) {
        ResponseEntity response = null;
        ErrorResponse error = null;

        long id = identification.getHeadertoken(accessToken);
        Optional<UserChecklist> getUserChecklistUserId = userChecklistDao.getByUserId(id);
        BasicMeta meta;
        if (getUserChecklistUserId.isEmpty()) {
            String errorMsg = "유저가 등록한 체크리스트가 없습니다.";
            meta = new BasicMeta(false, errorMsg);
            error = new ErrorResponse(meta);
            response = new ResponseEntity<>(error, HttpStatus.OK);
            return response;
        }


        if (getUserChecklistUserId.get().getId() == checklistId) {
            userChecklistDao.deleteById(checklistId);
            meta = new BasicMeta(true, "체크리스트가 성공적으로 삭제되었습니다.");
            SuccessResponse result = new SuccessResponse(meta);
            response = new ResponseEntity<>(result, HttpStatus.OK);
            return response;
        }


        String errorMsg = "해당 체크리스트가 존재하지 않습니다.";
        meta = new BasicMeta(false, errorMsg);
        error = new ErrorResponse(meta);
        response = new ResponseEntity<>(error, HttpStatus.OK);
        return response;
    }

    public void insertJobCheckList(String[] checkList, long jobId) {
        long idx = 0;

        for (String check : checkList) {
            long finalIdx = idx;
            jobCheckListDao.save(JobCheckList.builder().checkListId(finalIdx).contents(check).jobId(jobId).build());
            idx++;
        }
    }
}

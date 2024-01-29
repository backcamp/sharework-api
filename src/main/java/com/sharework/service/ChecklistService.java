package com.sharework.service;

import com.sharework.dao.JobCheckListDao;
import com.sharework.dao.UserChecklistDao;
import com.sharework.global.NotFoundException;
import com.sharework.manager.TokenIdentification;
import com.sharework.model.JobCheckList;
import com.sharework.model.UserChecklist;
import com.sharework.request.model.CheckListName;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.UserChecklistPayload;
import com.sharework.response.model.UserChecklistResponse;
import com.sharework.response.model.meta.BasicMeta;
import org.springframework.beans.factory.annotation.Autowired;
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

    public SuccessResponse insertUserChecklist(String accessToken, CheckListName checklistName) {
        long id = identification.getHeadertoken(accessToken);

        String[] beforeCheckList = checklistName.getCheckListName();
//        if (beforeCheckList.length == 0) {
//            String errorMsg = "체크리스트를 적어주세요.";
//            meta = new BasicMeta(true, errorMsg);
//            error = new ErrorResponse(meta);
//            response = new ResponseEntity<>(error, HttpStatus.OK);
//            return response;
//        }
        if (beforeCheckList.length > 5) {
            return new SuccessResponse(new BasicMeta(false, "체크리스트 제한을 넘었습니다."));
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

        return new SuccessResponse(new BasicMeta(true, "체크리스트가 성공적으로 저장되었습니다."));
    }

    public UserChecklistResponse getChecklist(String accessToken) {
        long id = identification.getHeadertoken(accessToken);

        Optional<UserChecklist> userChecklist = userChecklistDao.getByUserId(id);

        if (userChecklist.isEmpty()) {
            throw new NotFoundException("유저가 등록한 체크리스트가 없습니다.");
        }

        BasicMeta meta = new BasicMeta(true, "체크리스트 제공이 완료되었습니다.");
        UserChecklistPayload.Checklist checklist = new UserChecklistPayload.Checklist(id, userChecklist.get().getContents());
        UserChecklistPayload userChecklistPayload = new UserChecklistPayload(checklist);
        return new UserChecklistResponse(userChecklistPayload, meta);
    }

    public SuccessResponse delChecklist(String accessToken, long checklistId) {
        long id = identification.getHeadertoken(accessToken);
        Optional<UserChecklist> getUserChecklistUserId = userChecklistDao.getByUserId(id);
        if (getUserChecklistUserId.isEmpty()) {
            return new SuccessResponse(new BasicMeta(false, "유저가 등록한 체크리스트가 없습니다."));
        }


        if (getUserChecklistUserId.get().getId() == checklistId) {
            userChecklistDao.deleteById(checklistId);
            return new SuccessResponse(new BasicMeta(true, "체크리스트가 성공적으로 삭제되었습니다."));
        }


        return new SuccessResponse(new BasicMeta(false,"해당 체크리스트가 존재하지 않습니다."));
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

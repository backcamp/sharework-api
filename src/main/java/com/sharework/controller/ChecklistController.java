package com.sharework.controller;

import com.sharework.request.model.CheckListName;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.UserChecklistResponse;
import com.sharework.service.ChecklistService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/userCheckList") // FIXME: /checklist
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService userChecklistService;

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PatchMapping (produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "PATCH", value = "giver가 작성한 확인사항을 DB에 저장.", notes = "insert checklist")
    public ResponseEntity<SuccessResponse> insertUserChecklist(@RequestHeader("access-token") String accessToken,
                                             @RequestBody CheckListName checkListName) {
        SuccessResponse response = userChecklistService.insertUserChecklist(accessToken, checkListName);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = UserChecklistResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE}) // FIXME: remove value
    @ApiOperation(httpMethod = "GET", value = "giver가 전에 작성한 확인사항을 넘겨줌.", notes = "get checklist by userId")
    public ResponseEntity<UserChecklistResponse> getChecklist(@RequestHeader("access-token") String accessToken) {
        UserChecklistResponse response = userChecklistService.getChecklist(accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @DeleteMapping(value = "{checklistId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "DELETE", value = "giver가 작성한 체크리스트를 삭제.", notes = "delete checklist by userId")
    public ResponseEntity<SuccessResponse> delChecklist(@PathVariable(required = true) long checklistId, @RequestHeader("access-token") String accessToken) {
        SuccessResponse response = userChecklistService.delChecklist(accessToken, checklistId);
        return ResponseEntity.ok(response);
    }
}

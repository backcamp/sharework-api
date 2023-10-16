package com.sharework.controller;

import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.base_review.BaseReviewResponse;
import com.sharework.service.BaseReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/base-review")
@RequiredArgsConstructor
public class BaseReviewController {


    private final BaseReviewService baseReviewService;

//    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIBaseReview.class),
//            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
//    @GetMapping(value = "/worker", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @ApiOperation(httpMethod = "GET", value = "worker base review list")
//    public ResponseEntity getBaseReviewListWorker() {
//        return baseReviewService.getBaseReviewList(UserTypeEnum.WORKER.name());
//    }
//
//    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIBaseReview.class),
//            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
//    @GetMapping(value = "/giver", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @ApiOperation(httpMethod = "GET", value = "giver base review list")
//    public ResponseEntity getBaseReviewListGiver() {
//        return baseReviewService.getBaseReviewList(UserTypeEnum.GIVER.name());
//    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = BaseReviewResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "리뷰 체크리스트를 제공한다.", notes = "get baseReviewList")
    public ResponseEntity giveReviewCheckList(@RequestHeader("access-token") String accessToken) {
        return baseReviewService.getBaseReviewList(accessToken);
    }

}

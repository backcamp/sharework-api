package com.sharework.controller;

import com.sharework.request.model.RegisterReview;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.review.APIGetReview;
import com.sharework.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.GiveTagListResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIGetReview.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "사용자가 받은 리뷰를 제공한다.", notes = "give reviewInfo")
    public ResponseEntity<APIGetReview> getUserReview(@RequestHeader("access-token") String accessToken) {
        APIGetReview response = reviewService.giveUserReview(accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class)})
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "POST", value = "리뷰정보를 저장한다.", notes = "give reviewInfo")
    public ResponseEntity<SuccessResponse> insertReview(@RequestHeader("access-token") String accessToken, @RequestBody RegisterReview registerReview) {
        SuccessResponse response = reviewService.insertReview(accessToken, registerReview);
        return ResponseEntity.ok(response);
    }
}

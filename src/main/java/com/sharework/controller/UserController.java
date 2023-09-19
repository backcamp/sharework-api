package com.sharework.controller;

import com.sharework.request.model.APIUpdateUser;
import com.sharework.response.model.Response;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.tag.APIGetTagRank;
import com.sharework.response.model.user.APIGetUser;
import com.sharework.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(path = "/api/v3/user")
public class UserController {
    @Autowired
    UserService userService;

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIGetUser.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "유저 정보")
    public ResponseEntity getUser(@RequestHeader("access-token") String accessToken) {
        return userService.getUser(accessToken);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = Response.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @PatchMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "PATCH", value = "프로필 수정")
    public ResponseEntity updateUser(@RequestHeader("access-token") String accessToken,
                                     @Valid @RequestBody(required = true) APIUpdateUser request) {
        return userService.updateUser(accessToken, request);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIGetTagRank.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "/tag", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "유저 태그 정보")
    public ResponseEntity getTagRankUser(@RequestHeader("access-token") String accessToken) {
        return userService.getTagRank(accessToken);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @PostMapping(value = "/img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(httpMethod = "POST", value = "이미지 s3에 저장.")
    public ResponseEntity insertImg(@RequestHeader("access-token") String accessToken, @RequestPart("multipartFile") MultipartFile multipartFile) {
        return userService.insertImg(accessToken, multipartFile);
    }
    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "/img", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "유저 이미지url 전송.")
    public ResponseEntity getImg(@RequestHeader("access-token") String accessToken) {
        return userService.getImg(accessToken);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @DeleteMapping(value = "/img", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "DELETE", value = "유저 이미지 삭제")
    public ResponseEntity deleteImg(@RequestHeader("access-token") String accessToken) {
        return userService.deleteImg(accessToken);
    }
}

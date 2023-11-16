package com.sharework.controller;

import com.sharework.request.model.APIUpdateUser;
import com.sharework.response.model.ImgPayload;
import com.sharework.response.model.ImgResponse;
import com.sharework.response.model.Response;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.meta.BasicMeta;
import com.sharework.response.model.tag.APIGetTagRank;
import com.sharework.response.model.tag.APIGetTagRank.APIGetTagRankPayload;
import com.sharework.response.model.tag.TagRank;
import com.sharework.response.model.user.APIGetUser;
import com.sharework.response.model.user.APIGetUser.APIGetUserPayload;
import com.sharework.response.model.user.Profile;
import com.sharework.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<APIGetUser> getUser(@RequestHeader("access-token") String accessToken) {
        Profile profile = userService.getUser(accessToken);

        BasicMeta meta = new BasicMeta(true, "");
        APIGetUserPayload apiGetUserPayload = new APIGetUserPayload(profile);
        return ResponseEntity.ok(new APIGetUser(apiGetUserPayload, meta));
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = Response.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @PatchMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "PATCH", value = "프로필 수정")
    public ResponseEntity<SuccessResponse> updateUser(@RequestHeader("access-token") String accessToken,
                                     @Valid @RequestBody(required = true) APIUpdateUser request) {
        userService.updateUser(accessToken, request);

        BasicMeta meta = new BasicMeta(true, "정보가 수정되었습니다.");
        return ResponseEntity.ok(new SuccessResponse(meta));
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = APIGetTagRank.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "/tag", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "유저 태그 정보")
    public ResponseEntity<APIGetTagRank> getTagRankUser(@RequestHeader("access-token") String accessToken) {
        List<TagRank> tagRankList = userService.getTagRank(accessToken);

        BasicMeta meta = new BasicMeta(true, "");
        APIGetTagRankPayload apiGetTagRankPayload = new APIGetTagRankPayload(tagRankList);
        return ResponseEntity.ok(new APIGetTagRank(apiGetTagRankPayload, meta));
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @PostMapping(value = "/img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(httpMethod = "POST", value = "이미지 s3에 저장.")
    public ResponseEntity<SuccessResponse> insertImg(@RequestHeader("access-token") String accessToken, @RequestPart("multipartFile") MultipartFile multipartFile) {
        boolean success = userService.insertImg(accessToken, multipartFile);

        BasicMeta meta;
        if (success) {
            meta = new BasicMeta(true, "이미지가 성공적으로 저장되었습니다.");
        } else {
            meta = new BasicMeta(false, "유저 정보가 존재하지 않습니다.");
        }
        return ResponseEntity.ok(new SuccessResponse(meta));
    }
    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @GetMapping(value = "/img", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "GET", value = "유저 이미지url 전송.")
    public ResponseEntity<ImgResponse> getImg(@RequestHeader("access-token") String accessToken) {
        String imgUrl = userService.getImg(accessToken);

        BasicMeta meta;
        if (imgUrl != "") {
            meta = new BasicMeta(true, "");
        } else {
            meta = new BasicMeta(false, "유저 정보가 존재하지 않습니다.");
        }
        ImgPayload imgPayload = new ImgPayload(imgUrl);
        return ResponseEntity.ok(new ImgResponse(imgPayload, meta));
    }

    @ApiResponses({@ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = Response.class)})
    @DeleteMapping(value = "/img", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(httpMethod = "DELETE", value = "유저 이미지 삭제")
    public ResponseEntity<SuccessResponse> deleteImg(@RequestHeader("access-token") String accessToken) {
        boolean success = userService.deleteImg(accessToken);

        BasicMeta meta;
        if (success) {
            meta = new BasicMeta(true, "성공적으로 삭제하였습니다.");
        } else {
            meta = new BasicMeta(false, "이미지가 존재하지 않거나, 유저 정보가 존재하지 않습니다.");
        }
        return ResponseEntity.ok(new SuccessResponse(meta));
    }
}

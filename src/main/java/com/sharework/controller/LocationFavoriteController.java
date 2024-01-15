package com.sharework.controller;

import com.sharework.model.LocationFavorite;
import com.sharework.request.model.LocationFavoriteRequest;
import com.sharework.response.model.*;
import com.sharework.response.model.location.APIgetLoactionFavoriteList;
import com.sharework.service.LocationFavoriteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping(path = "/api/v3/locationFavorite")
@RequiredArgsConstructor
public class LocationFavoriteController {
    private final LocationFavoriteService locationFavoriteService;

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = APIgetLoactionFavoriteList.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "GET", value = "유저가 등록한 위치 즐겨찾기 리스트")
    public ResponseEntity<APIgetLoactionFavoriteList> getLocationFavoriteList(@RequestHeader("access-token") String accessToken) {
        APIgetLoactionFavoriteList response = locationFavoriteService.getLocationFavoriteList(accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @PostMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "POST", value = "위치 정보를 받아 DB에 저장")
    public ResponseEntity<SuccessResponse> insertLocationFavorite(@RequestBody @ApiParam(required = true) LocationFavoriteRequest locationFavoriteRequest,
                                                  @RequestHeader("access-token") String accessToken) {
        SuccessResponse response = locationFavoriteService.insertLocationFavorite(locationFavoriteRequest, accessToken);
        return ResponseEntity.ok(response);
    }

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = SuccessResponse.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "DELETE", value = "위치 정보 삭제")
    public ResponseEntity<SuccessResponse> deleteLocationFavorite(@PathVariable long id) {
        SuccessResponse response = locationFavoriteService.deleteLocationFavorite(id);
        return ResponseEntity.ok(response);
    }
}

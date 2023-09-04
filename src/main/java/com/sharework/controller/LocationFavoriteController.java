package com.sharework.controller;

import com.sharework.model.LocationFavorite;
import com.sharework.request.model.LocationFavoriteRequest;
import com.sharework.response.model.*;
import com.sharework.service.LocationFavoriteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping(path = "/api/v3/locationFavorite")
public class LocationFavoriteController {
    @Autowired
    LocationFavoriteService locationFavoriteService;

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = Response.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "GET", value = "유저가 등록한 위치 즐겨찾기 리스트")
    public ResponseEntity getLocationFavoriteList(@RequestHeader("access-token") String accessToken) {
        return locationFavoriteService.getLocationFavoriteList(accessToken);
    }

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = Response.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @PostMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "POST", value = "위치 정보를 받아 DB에 저장")
    public ResponseEntity insertLocationFavorite(@RequestBody @ApiParam(required = true) LocationFavoriteRequest locationFavoriteRequest,
                                                  @RequestHeader("access-token") String accessToken) {
        return locationFavoriteService.insertLocationFavorite(locationFavoriteRequest, accessToken);
    }

    @ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = Response.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(httpMethod = "DELETE", value = "위치 정보 삭제")
    public ResponseEntity deleteLocationFavorite(@PathVariable long id) {
        return locationFavoriteService.deleteLocationFavorite(id);
    }
}

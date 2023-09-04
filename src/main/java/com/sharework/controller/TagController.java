package com.sharework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sharework.response.model.ErrorResponse;
import com.sharework.response.model.GiveTagListResponse;
import com.sharework.service.TagService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping(path = "/api/v3")
public class TagController {

	@Autowired
	TagService tagService;

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = GiveTagListResponse.class),
			@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@GetMapping(value = "/tag", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "GET", value = "태그 목록을 제공한다.", notes = "give tagList")
	public ResponseEntity giveTagList() {
		return tagService.giveTagList();
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "SUCCESS", response = GiveTagListResponse.class),
		@ApiResponse(code = 404, message = "NOT FOUND", response = ErrorResponse.class) })
	@GetMapping(value = "/alltag", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(httpMethod = "GET", value = "전체 태그 목록을 제공한다.", notes = "give allTagList")
	public ResponseEntity giveAllTagList() {
		return tagService.giveAllTagList();
	}
}

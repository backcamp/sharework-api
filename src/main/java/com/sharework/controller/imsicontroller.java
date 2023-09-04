package com.sharework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sharework.dao.JobDao;
import com.sharework.dao.TagDao;
import com.sharework.dao.TagSubDao;
import com.sharework.dao.UserDao;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class TestController
 */
@RestController
public class imsicontroller {

	@Autowired
	UserDao userdao;

	@Autowired
	JobDao jobdao;
	@Autowired
	TagDao tagDao;

	@Autowired
	TagSubDao tagSubDao;

	@GetMapping(value = "/hello")
	@ApiOperation(value = "hello, world api", notes = "hellow world swagger check")
	public String hellowWorld(@RequestHeader("access-token") String accessToken) {
		System.out.println(accessToken);
		return "hello, world";
	}

	@ApiOperation(value = "test", notes = "테스트입니다")
	@ApiResponses({ @ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 404, message = "page not found!") })
	@GetMapping(value = "/board")
	public Map<String, String> selectBoard(
			@ApiParam(value = "샘플번호", required = true, example = "1") @RequestParam String no) {

		Map<String, String> result = new HashMap<>();
		result.put("test title", "1231231231231");
		result.put("test contents", "테스트 내용");
		return result;
	}
}

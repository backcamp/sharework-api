package com.sharework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharework.response.model.SuccessResponse;
import com.sharework.response.model.user.APIGetUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController 클래스")
class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private ResultActions performGet(String accessToken) throws Exception {
//        return mockMvc.perform(get("/api/v3/user")
//                .contentType(MediaType.APPLICATION_JSON).header("access-token",accessToken));
//    }
//
//    @Nested
//    @DisplayName("GET 유저정보 테스트")
//    class Describe_PATCH_job_closed {
//        String VALID_ACCESS_TOKEN;
//
//
//        @BeforeEach
//        void setUp() {
//            VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VydHlwZSI6IndvcmtlciIsInVzZXJpZCI6MTAsImlhdCI6MTY3Mzg2MjM0MiwiZXhwIjoxNjc0NDY3MTQyfQ.h6moETEnkOQ8PVch0g-VUuYumenLJ-EPB84JCREuGSE";
//        }
//
//        @Nested
//        @DisplayName("헤더에 기존 토큰 정보가 누락된 요청이 들어오면")
//        class Context_no_valid_token_in_header {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performGet(null)
//                        .andExpect(status().isBadRequest());
//
//            }
//        }
//
//        @Nested
//        @DisplayName("정상적인 access-token이 들어온다면")
//        class Context_valid_token_in_header {
//            @Test
//            @DisplayName("200 OK와 SuccessResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performGet(VALID_ACCESS_TOKEN)
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//
//                String content = mvcResult.getResponse().getContentAsString();
//                APIGetUser getUser = objectMapper.readValue(content, APIGetUser.class);
//                  assertThat(getUser.getPayload()).isNotNull();
////                assertThat(signUpResponse.getPayload().getAccessToken()).containsPattern("^[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+$");
////                assertThat(successResponse.getMeta().getMessage());
//            }
//        }
//    }
}
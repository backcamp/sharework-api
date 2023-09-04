package com.sharework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharework.response.model.SignUpResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("JwtController 클래스")
class JwtControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private ResultActions performPost(String accessToken, String refreshToken) throws Exception {
//        return mockMvc.perform(post("/api/v3/jwt")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("access-token", accessToken)
//                .header("refresh-token", refreshToken));
//    }
//
//    @Nested
//    @DisplayName("POST /api/v3/jwt")
//    class Describe_POST_api_v3_jwt {
//        String VALID_ACCESS_TOKEN;
//        String VALID_REFRESH_TOKEN;
//
//        @BeforeEach
//        void setUp() {
//            VALID_ACCESS_TOKEN = ""; // FIXME - 정상적인 JWT 토큰 생성 로직 추가
//            VALID_REFRESH_TOKEN = ""; // FIXME - 정상적인 JWT 토큰 생성 로직 추가
//        }
//
//        @Nested
//        @DisplayName("헤더에 기존 토큰 정보가 누락된 요청이 들어오면")
//        class Context_no_token_in_header {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performPost(null, null)
//                        .andExpect(status().isBadRequest());
//
//                performPost(VALID_ACCESS_TOKEN, null)
//                        .andExpect(status().isBadRequest());
//
//                performPost(null, VALID_REFRESH_TOKEN)
//                        .andExpect(status().isBadRequest());
//            }
//        }
//
//        @Nested
//        @DisplayName("헤더에 정상적인 토큰 정보가 들어오면")
//        class Context_valid_token_in_header {
//
//            @Test
//            @DisplayName("200 OK와 SignUpResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performPost(VALID_ACCESS_TOKEN, VALID_REFRESH_TOKEN)
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//                String content = mvcResult.getResponse().getContentAsString();
//                SignUpResponse signUpResponse = mapper.readValue(content, SignUpResponse.class);
//
//                assertThat(signUpResponse.getPayload().getAccessToken()).containsPattern("^[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+$");
//                assertThat(signUpResponse.getPayload().getRefreshToken()).containsPattern("^[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+$");
//            }
//        }
//    }
}
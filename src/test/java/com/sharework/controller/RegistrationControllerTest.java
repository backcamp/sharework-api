package com.sharework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharework.request.model.SignInRequestPw;
import com.sharework.request.model.SignupRequestPw;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("RegistrationController")
class RegistrationControllerTest {

//    static {
//        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    private ResultActions performPost(String signupRequestPw) throws Exception {
//        return mockMvc.perform(post("/api/v3/registration")
//                .contentType(MediaType.APPLICATION_JSON).content(signupRequestPw));
//    }
//
//    @Nested
//    @DisplayName("POST 회원가입 테스트")
//    class Describe_POST_Registration {
//        SignupRequestPw signupRequestPw = new SignupRequestPw();
//
//
//        @BeforeEach
//        void setUp() {
//            signupRequestPw.setEmail("sharework12@sharework.com");
//            signupRequestPw.setName("이테스");
//            signupRequestPw.setPhoneNumber("01044124478");
//            signupRequestPw.setResidentNumberFront("930924");
//            signupRequestPw.setResidentNumberRear("2");
//            signupRequestPw.setPassword("testT12!!!??");
//
//        }
//
//        @Nested
//        @DisplayName("누락된 값이 들어온다면")
//        class Context_no_valid_requestBody {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performPost(null)
//                        .andExpect(status().isBadRequest());
//
//            }
//        }
//
//        @Nested
//        @DisplayName("정상적인 requestBody가 들어온다면")
//        class Context_valid_requestBody {
//            @Test
//            @DisplayName("200 OK와 SuccessResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performPost(objectMapper.writeValueAsString(signupRequestPw))
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//
//                String content = mvcResult.getResponse().getContentAsString();
//                System.out.println(content.toString());
////                APIGetUser getUser = objectMapper.readValue(content, APIGetUser.class);
////                assertThat(getUser.getPayload()).isNotNull();
////                assertThat(signUpResponse.getPayload().getAccessToken()).containsPattern("^[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+$");
////                assertThat(successResponse.getMeta().getMessage());
//            }
//        }
//    }
//
//    private ResultActions performLogin(String signInRequestPw) throws Exception {
//        return mockMvc.perform(post("/api/v3/registration/login")
//                .contentType(MediaType.APPLICATION_JSON).content(signInRequestPw));
//    }
//
//    @Nested
//    @DisplayName("POST 로그인 테스트")
//    class Describe_POST_Login {
//        SignInRequestPw signInRequestPw = new SignInRequestPw();
//
//        @BeforeEach
//        void setUp() {
//            signInRequestPw.setPhoneNumber("01021253653");
//            signInRequestPw.setPassword("Dkqjwl124!?");
//
//        }
//
//        @Nested
//        @DisplayName("누락된 값이 들어온다면")
//        class Context_no_valid_requestBody {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performPost(null)
//                        .andExpect(status().isBadRequest());
//
//            }
//        }
//
//        @Nested
//        @DisplayName("정상적인 requestBody가 들어온다면")
//        class Context_valid_requestBody {
//            @Test
//            @DisplayName("200 OK와 SuccessResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performPost(objectMapper.writeValueAsString(signInRequestPw))
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//
//                String content = mvcResult.getResponse().getContentAsString();
//                System.out.println(content.toString());
////                APIGetUser getUser = objectMapper.readValue(content, APIGetUser.class);
////                assertThat(getUser.getPayload()).isNotNull();
////                assertThat(signUpResponse.getPayload().getAccessToken()).containsPattern("^[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-_]+$");
////                assertThat(successResponse.getMeta().getMessage());
//            }
//        }
//    }
}
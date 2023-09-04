package com.sharework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharework.response.model.SuccessResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("JobController 클래스")
class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

//    private ResultActions performPatch(Long id) throws Exception {
//        return mockMvc.perform(patch("/api/v3/job/closed/{id}", id)
//                .contentType(MediaType.APPLICATION_JSON));
//    }
//
//    private ResultActions performGet(String accessToken, long id) throws Exception {
//        return mockMvc.perform(get("/api/v3/job/info/{id}", id)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("access-token", accessToken));
//    }

//    @Nested
//    @DisplayName("/api/v3/job/closed 테스트")
//    class Describe_PATCH_job_closed {
//        long id;
//
//
//        @BeforeEach
//        void setUp() {
//            id = 174;
//        }
//
//        @Nested
//        @DisplayName("id가 누락되서 들어온다면")
//        class context_no_id_in_PATCH {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performPatch(null)
//                        .andExpect(status().isBadRequest());
//
//            }
//        }
//
//        @Nested
//        @DisplayName("정상적인 Id가 들어온다면")
//        class context_id_in_PATCH {
//            @Test
//            @DisplayName("200 OK와 SuccessResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performPatch(id)
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//
//                String content = mvcResult.getResponse().getContentAsString();
//                System.out.println("content" + content);
//                SuccessResponse successResponse = objectMapper.readValue(content, SuccessResponse.class);
////                assertThat(successResponse.getMeta().getMessage());
////                assertThat(successResponse.getMeta().getMessage());
//            }
//        }
//    }

//    @Nested
//    @DisplayName("/api/v3/job/info/{id} 테스트")
//    class Describe_GET_job_info {
//        long id;
//        String VALID_ACCESS_TOKEN;
//
//        @BeforeEach
//        void setUp() {
//            id = 211;
//            VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VydHlwZSI6ImdpdmVyIiwidXNlcmlkIjoxMCwiaWF0IjoxNjc1NjAyNDc1LCJleHAiOjE2NzYyMDcyNzV9.uQJiFlAn8Ixn3umQndVtZ5b4dkuZjrdUFrZyODfNAcw";
//
//        }
//
//        @Nested
//        @DisplayName("id가 누락되서 들어온다면")
//        class context_no_id_in_GET {
//
//            @Test
//            @DisplayName("400 BAD REQUEST 에러를 반환한다.")
//            void it_returns_400() throws Exception {
//                performGet(null, 0)
//                        .andExpect(status().isBadRequest());
//
//            }
//        }

//        @Nested
//        @DisplayName("정상적인 Id가 들어온다면")
//        class context_id_in_GET {
//            @Test
//            @DisplayName("200 OK와 SuccessResponse를 반환한다.")
//            void it_returns_200() throws Exception {
//                MvcResult mvcResult = performGet(VALID_ACCESS_TOKEN, id)
//                        .andExpect(status().isOk())
//                        .andReturn();
//
//
//                System.out.println(mvcResult.getResponse().getContentAsString());
//                String content = mvcResult.getResponse().getContentAsString();
//                System.out.println("content" + content);
//                SuccessResponse successResponse = objectMapper.readValue(content, SuccessResponse.class);
//                assertThat(successResponse.getMeta().getMessage());
//                assertThat(successResponse.getMeta().getMessage());
//            }
//        }
//    }
}
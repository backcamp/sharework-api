package com.sharework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ApplicationController 클래스")
class ApplicationControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    final String ACCESS_TOKEN = "";
//
//    private ResultActions performPost(String url, Object object) throws Exception {
//        return mockMvc.perform(post(url)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .header("access-token", ACCESS_TOKEN)
//                    .content(mapper.writeValueAsString(object)));
//    }
//
//    private ResultActions performGet(String url) throws Exception {
//        return mockMvc.perform(get(url)
//                    .header("access-token", ACCESS_TOKEN));
//    }
//
//    private ResultActions performDelete(String url) throws Exception {
//        return mockMvc.perform(delete(url)
//                .header("access-token", ACCESS_TOKEN));
//    }
//
//    private ResultActions performPut(String url, Object object) throws Exception {
//        return mockMvc.perform(put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("access-token", ACCESS_TOKEN)
//                .content(mapper.writeValueAsString(object)));
//    }
//
//    private ResultActions performPatch(String url, Object object) throws Exception {
//        return mockMvc.perform(patch(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("access-token", ACCESS_TOKEN)
//                .content(mapper.writeValueAsString(object)));
//    }
//
//    @Nested
//    @DisplayName("POST /api/v3/application")
//    class Describe_POST_application {
//        @Nested
//        @DisplayName("OOO한 상황에서")
//        class Context_success {
//            @Test
//            @DisplayName("OOO를 리턴한다")
//            void it_returns_new_task() throws Exception {
//            }
//        }
//    }
}
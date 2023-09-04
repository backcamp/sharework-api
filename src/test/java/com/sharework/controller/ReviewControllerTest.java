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
import org.springframework.scheduling.config.Task;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ReviewController 클래스")
class ReviewControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    private final ObjectMapper mapper = new ObjectMapper();

//    @Test
//    void performPost() throws Exception {
//        mockMvc.perform(get("/api/v3/user/review")
//                        .header("access-token", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VydHlwZSI6IndvcmtlciIsInVzZXJpZCI6MTAsImlhdCI6MTY2NDEyMzE1NSwiZXhwIjoxNjY0NzI3OTU1fQ.RPwYEj4t53EKLz5z7119nyLwfiZ5FKG91e_AYPbxPmo"))
//                .andExpect(status().isOk())
//                .andReturn();
//    }


}
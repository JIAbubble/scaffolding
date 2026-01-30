package com.example.performanceevaluation.controller;

import com.example.performanceevaluation.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController测试示例
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testListUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }
    
    @Test
    void testCreateUser() throws Exception {
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role("USER")
                .build();
        
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }
}


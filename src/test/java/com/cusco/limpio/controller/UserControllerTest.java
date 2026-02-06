package com.cusco.limpio.controller;

import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ShouldReturnCreated_WhenValidRequest() throws Exception {
        // Given
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "test@example.com",
                "password123",
                "John",
                "Doe",
                "999999999",
                "USER"
        );

        UserDTO userDTO = new UserDTO(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "999999999",
                "USER",
                true,
                LocalDateTime.now()
        );

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }
}

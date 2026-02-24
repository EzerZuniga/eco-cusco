package com.cusco.limpio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_ShouldReturnCreated_WhenValidRequest() throws Exception {
        // Given
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "test@example.com",
                "password123",
                "John",
                "Doe",
                "+51999999999",
                "CITIZEN");

        UserDTO userDTO = new UserDTO(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "+51999999999",
                "CITIZEN",
                true,
                LocalDateTime.now());

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(userDTO);

        // When & Then
        // El controlador retorna directamente el UserDTO, no envuelto en "data"
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}

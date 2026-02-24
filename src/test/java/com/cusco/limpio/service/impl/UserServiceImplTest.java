package com.cusco.limpio.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.exception.BadRequestException;
import com.cusco.limpio.mapper.UserMapper;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserDTO createUserDTO;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        createUserDTO = new CreateUserDTO(
                "test@example.com",
                "password123",
                "John",
                "Doe",
                "+51999999999",
                "CITIZEN");

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .phone("+51999999999")
                .role(User.UserRole.CITIZEN)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userDTO = new UserDTO(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "+51999999999",
                "CITIZEN",
                true,
                LocalDateTime.now());
    }

    @Test
    void createUser_ShouldReturnUserDTO_WhenEmailDoesNotExist() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.createUser(createUserDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).existsByEmail(createUserDTO.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowBadRequestException_WhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createUserDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("El email ya está registrado");

        verify(userRepository, times(1)).existsByEmail(createUserDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(userRepository, times(1)).findById(1L);
    }
}

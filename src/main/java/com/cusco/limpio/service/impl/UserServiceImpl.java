package com.cusco.limpio.service.impl;

import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.user.AuthResponseDTO;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.dto.user.LoginDTO;
import com.cusco.limpio.exception.UnauthorizedException;
import com.cusco.limpio.exception.ResourceNotFoundException;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.security.JwtTokenProvider;
import com.cusco.limpio.service.UserService;
import com.cusco.limpio.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(createUserDTO.email())
                .password(passwordEncoder.encode(createUserDTO.password()))
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .phone(createUserDTO.phone())
                .role(User.UserRole.valueOf(createUserDTO.role().toUpperCase()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, CreateUserDTO updateUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFirstName(updateUserDTO.firstName());
        user.setLastName(updateUserDTO.lastName());
        user.setPhone(updateUserDTO.phone());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.getActive()) {
            throw new UnauthorizedException("User account is deactivated");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponseDTO.of(token, jwtExpirationMs, userDTO);
    }
}

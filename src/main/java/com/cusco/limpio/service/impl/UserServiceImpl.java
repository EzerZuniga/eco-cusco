package com.cusco.limpio.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.user.AuthResponseDTO;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.LoginDTO;
import com.cusco.limpio.dto.user.UpdateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.exception.BadRequestException;
import com.cusco.limpio.exception.ForbiddenException;
import com.cusco.limpio.exception.ResourceNotFoundException;
import com.cusco.limpio.exception.UnauthorizedException;
import com.cusco.limpio.mapper.UserMapper;
import com.cusco.limpio.repository.UserRepository;
import com.cusco.limpio.security.JwtTokenProvider;
import com.cusco.limpio.service.UserService;

import lombok.RequiredArgsConstructor;

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
            throw new BadRequestException("El email ya está registrado");
        }

        User.UserRole role;
        try {
            role = User.UserRole.valueOf(createUserDTO.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Rol inválido: " + createUserDTO.role());
        }

        User user = User.builder()
                .email(createUserDTO.email())
                .password(passwordEncoder.encode(createUserDTO.password()))
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .phone(createUserDTO.phone())
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(findUserOrThrow(id));
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
    public UserDTO updateUser(Long id, UpdateUserDTO dto, String authenticatedEmail) {
        User user = findUserOrThrow(id);
        assertOwnerOrAdmin(user, authenticatedEmail);

        // Actualización parcial: solo se modifican los campos que el cliente envía (no
        // nulos)
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.phone() != null) {
            user.setPhone(dto.phone());
        }

        // Cambio de contraseña es opcional: solo si se envía newPassword
        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id, String authenticatedEmail) {
        User user = findUserOrThrow(id);
        assertOwnerOrAdmin(user, authenticatedEmail);
        // Baja lógica: no se elimina físicamente para preservar integridad referencial
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new UnauthorizedException("La cuenta está desactivada");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return AuthResponseDTO.of(token, jwtExpirationMs, userMapper.toDTO(user));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    // Verifica que el usuario autenticado sea el propietario del recurso o un ADMIN
    private void assertOwnerOrAdmin(User targetUser, String authenticatedEmail) {
        boolean isOwner = targetUser.getEmail().equalsIgnoreCase(authenticatedEmail);
        boolean isAdmin = targetUser.getRole() == User.UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("No tienes autorización para modificar este usuario");
        }
    }
}

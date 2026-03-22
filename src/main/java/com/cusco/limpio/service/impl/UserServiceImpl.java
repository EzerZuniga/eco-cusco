package com.cusco.limpio.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

@Service
public class UserServiceImpl implements UserService {

    private static final User.UserRole PUBLIC_REGISTRATION_ROLE = User.UserRole.CITIZEN;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        String normalizedEmail = normalizeEmail(createUserDTO.email());
        if (!StringUtils.hasText(normalizedEmail)) {
            throw new BadRequestException("El email es obligatorio");
        }
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BadRequestException("El email ya está registrado");
        }

        User.UserRole role = resolveRegistrationRole(createUserDTO.role());
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .email(normalizedEmail)
                .password(passwordEncoder.encode(createUserDTO.password()))
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .phone(createUserDTO.phone())
                .role(role)
                .active(true)
                .createdAt(now)
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
        applyPartialUpdate(user, dto);
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
        String normalizedEmail = normalizeEmail(loginDTO.email());
        if (!StringUtils.hasText(normalizedEmail)) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
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

    private void applyPartialUpdate(User user, UpdateUserDTO dto) {
        if (StringUtils.hasText(dto.firstName())) {
            user.setFirstName(dto.firstName().trim());
        }
        if (StringUtils.hasText(dto.lastName())) {
            user.setLastName(dto.lastName().trim());
        }
        if (StringUtils.hasText(dto.phone())) {
            user.setPhone(dto.phone().trim());
        }

        // La contraseña se actualiza solo cuando el cliente la envía explícitamente.
        if (StringUtils.hasText(dto.newPassword())) {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
        }
    }

    // Verifica que el usuario autenticado sea el propietario del recurso o un ADMIN.
    private void assertOwnerOrAdmin(User targetUser, String authenticatedEmail) {
        String normalizedAuthenticatedEmail = normalizeEmail(authenticatedEmail);
        if (!StringUtils.hasText(normalizedAuthenticatedEmail)) {
            throw new UnauthorizedException("Usuario autenticado no encontrado");
        }

        boolean isOwner = targetUser.getEmail().equalsIgnoreCase(normalizedAuthenticatedEmail);

        if (isOwner) {
            return;
        }

        User authenticatedUser = userRepository.findByEmailIgnoreCase(normalizedAuthenticatedEmail)
                .orElseThrow(() -> new UnauthorizedException("Usuario autenticado no encontrado"));
        boolean isAdmin = authenticatedUser.getRole() == User.UserRole.ADMIN;
        if (!isAdmin) {
            throw new ForbiddenException("No tienes autorización para modificar este usuario");
        }
    }

    private User.UserRole resolveRegistrationRole(String requestedRole) {
        if (!StringUtils.hasText(requestedRole)) {
            return PUBLIC_REGISTRATION_ROLE;
        }

        User.UserRole parsedRole;
        try {
            parsedRole = User.UserRole.valueOf(requestedRole.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Rol inválido: " + requestedRole);
        }

        if (parsedRole != PUBLIC_REGISTRATION_ROLE) {
            throw new BadRequestException("El registro público solo permite el rol CITIZEN");
        }
        return parsedRole;
    }

    private String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}

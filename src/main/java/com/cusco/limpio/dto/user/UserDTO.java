package com.cusco.limpio.dto.user;

import java.time.LocalDateTime;

public record UserDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,
    String role,
    Boolean active,
    LocalDateTime createdAt
) {}
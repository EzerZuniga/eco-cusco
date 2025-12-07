package com.cusco.limpio.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Data Transfer Object para usuarios.
 * Incluye configuración de serialización para fechas y omite nulos.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,
    String role,
    Boolean active,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt
) {}
package com.cusco.limpio.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para autenticación
 * Contiene el token JWT y la información del usuario autenticado
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponseDTO(
    String token,
    String tokenType,
    Long expiresIn,
    UserDTO user
) {
    /**
     * Constructor con valores por defecto para tokenType
     */
    public static AuthResponseDTO of(String token, Long expiresInMs, UserDTO user) {
        return new AuthResponseDTO(token, "Bearer", expiresInMs, user);
    }
}

package com.cusco.limpio.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponseDTO(
    String token,
    String tokenType,
    Long expiresIn,
    UserDTO user
) {
    public static AuthResponseDTO of(String token, Long expiresInMs, UserDTO user) {
        return new AuthResponseDTO(token, "Bearer", expiresInMs, user);
    }
}

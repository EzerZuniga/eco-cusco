package com.cusco.limpio.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email debe ser válido") String email,

        @NotBlank(message = "La contraseña es obligatoria") String password) {
}

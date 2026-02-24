package com.cusco.limpio.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres") String firstName,

        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres") String lastName,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "El teléfono debe ser válido (9-15 dígitos)") String phone,

        @Size(min = 6, max = 100, message = "La nueva contraseña debe tener entre 6 y 100 caracteres") String newPassword) {
}

package com.cusco.limpio.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    String password,
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    String firstName,
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    String lastName,
    
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "El teléfono debe ser válido (9-15 dígitos)")
    String phone,
    
    @Pattern(regexp = "^(CITIZEN|MUNICIPAL_AGENT|ADMIN)$", message = "El rol debe ser CITIZEN, MUNICIPAL_AGENT o ADMIN")
    String role
) {}
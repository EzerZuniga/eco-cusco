package com.cusco.limpio.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6) String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String phone,
    String role
) {}
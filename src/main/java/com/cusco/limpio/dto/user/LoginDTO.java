package com.cusco.limpio.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank String email,
    @NotBlank String password
) {}
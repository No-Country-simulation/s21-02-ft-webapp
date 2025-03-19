package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Email(message = "El email debe ser válido") String email,
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")   String password
) {}

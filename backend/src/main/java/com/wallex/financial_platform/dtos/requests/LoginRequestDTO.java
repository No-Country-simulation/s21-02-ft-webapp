package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Credenciales para iniciar sesión")
public record LoginRequestDTO(
        @Email(message = "El email debe ser válido")
        @Schema(description = "Correo electrónico", example = "juan.perez@email.com", required = true)
        String email,

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Schema(description = "Contraseña", example = "Password123", required = true)
        String password
) {}
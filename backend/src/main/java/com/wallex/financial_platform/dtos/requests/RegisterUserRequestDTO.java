package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar un nuevo usuario")
public record RegisterUserRequestDTO(
        @NotBlank
        @Schema(description = "Nombre completo", example = "Juan Pérez", required = true)
        String fullName,

        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos")
        @Schema(description = "DNI (8 dígitos)", example = "12345678", required = true)
        String dni,

        @Email(message = "El email debe ser válido")
        @Schema(description = "Correo electrónico", example = "juan.perez@email.com", required = true)
        String email,

        @Pattern(regexp = "^\\+54\\d{10}$", message = "El teléfono debe seguir el formato: +54XXXXXXXXXX")
        @Schema(description = "Teléfono argentino", example = "+541112345678", required = true)
        String phoneNumber,

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Schema(description = "Contraseña (mínimo 8 caracteres)",
                example = "Password123",
                required = true)
        String password
) {}
package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
        @NotBlank String fullName,
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos") String dni,
        @Email(message = "El email debe ser válido") @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "El email debe seguir el formato: ejemplo@dominio.com") String email,
        @Pattern(regexp = "^\\+54\\d{10}$", message = "El teléfono debe seguir el formato: +54XXXXXXXXXX") String phoneNumber,
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password
) {}

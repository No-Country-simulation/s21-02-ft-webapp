package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Información de usuario")
public record UserResponseDTO(
        @Schema(description = "ID del usuario", example = "1")
        Long id,

        @Schema(description = "Nombre completo", example = "Juan Pérez")
        String fullName,

        @Schema(description = "DNI (8 dígitos)", example = "12345678")
        String dni,

        @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
        String email,

        @Schema(description = "Número de teléfono", example = "+541112345678")
        String phoneNumber,

        @Schema(description = "Fecha de creación", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Fecha de última actualización", example = "2024-01-15T14:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "Estado de la cuenta", example = "true")
        Boolean active
) {}
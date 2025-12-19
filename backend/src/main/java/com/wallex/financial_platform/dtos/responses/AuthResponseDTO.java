package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación")
public record AuthResponseDTO(
        @Schema(description = "Token JWT para autorización",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVtYWlsLmNvbSIsImlhdCI6MTYxMDIyNzIyMiwiZXhwIjoxNjEwMzEzNjIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String token,

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        String fullName,

        @Schema(description = "Email del usuario", example = "juan.perez@email.com")
        String email) {
}
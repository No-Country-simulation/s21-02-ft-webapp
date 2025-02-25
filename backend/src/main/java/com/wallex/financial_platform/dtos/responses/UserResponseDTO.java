package com.wallex.financial_platform.dtos.responses;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String fullName,
        String dni,
        String email,
        String phoneNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean active
) {}
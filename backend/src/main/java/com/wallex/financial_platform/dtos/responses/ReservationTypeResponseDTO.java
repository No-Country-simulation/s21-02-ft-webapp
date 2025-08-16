package com.wallex.financial_platform.dtos.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationTypeResponseDTO(
        Long reservationTypeId,
        String name,
        String iconUrl
) {}
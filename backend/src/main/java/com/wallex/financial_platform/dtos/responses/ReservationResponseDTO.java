package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.SuggestedReserve;
import com.wallex.financial_platform.entities.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponseDTO(
        Long reservationId,
        Long accountId,
        BigDecimal reservedAmount,
        LocalDateTime creationDate,
        ReservationStatus status,
        String reason
) {
}

package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.entities.enums.TypeReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponseDTO(
        Long reservationId,
        Long accountId,
        BigDecimal reservedAmount,
        LocalDateTime creationDate,
        ReservationStatus status,
        TypeReservation type
) {
}

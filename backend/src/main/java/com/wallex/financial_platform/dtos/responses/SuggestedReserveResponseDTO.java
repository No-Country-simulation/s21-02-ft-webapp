package com.wallex.financial_platform.dtos.responses;

public record SuggestedReserveResponseDTO(
        Long reservationTypeId,
        String name,
        String iconUrl
) {}
package com.wallex.financial_platform.dtos.responses;


public record ValidateTransferResultResponseDTO(
        boolean isValid,
        String accountName,
        String type) {
}


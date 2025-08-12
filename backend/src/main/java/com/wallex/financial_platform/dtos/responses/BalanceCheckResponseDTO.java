package com.wallex.financial_platform.dtos.responses;

import java.math.BigDecimal;

public record BalanceCheckResponseDTO(
        boolean hasEnoughBalance,
        BigDecimal currentBalance,
         String currency) {
}

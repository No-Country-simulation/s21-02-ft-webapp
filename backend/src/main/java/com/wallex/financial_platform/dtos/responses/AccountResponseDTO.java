package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.CurrencyType;

import java.math.BigDecimal;


public record AccountResponseDTO(
        String cbu,
        String alias,
        CurrencyType currency,
        BigDecimal balance
) {}

record BalanceResponseDto(
    BigDecimal available,
    BigDecimal reserved,
    BigDecimal pending,
    BigDecimal failed
) {

}
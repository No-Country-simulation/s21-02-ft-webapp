package com.wallex.financial_platform.dtos.requests;

import com.wallex.financial_platform.entities.enums.CurrencyType;

public record AccountRequestDto(
        CurrencyType currency
) {
}

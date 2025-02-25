package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.responses.AccountResponseDTO;

import java.util.List;

public interface IAccountService {
    List<AccountResponseDTO> getByUser();

}

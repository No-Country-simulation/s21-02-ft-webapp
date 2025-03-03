package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.CheckAccountRequestDto;
import com.wallex.financial_platform.dtos.responses.*;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAccountService {
    List<AccountResponseDTO> getByUser();
    CheckAccountResponseDTO checkAccount(CheckAccountRequestDto chkAcc);
    AccountResponseDTO createAccount(AccountRequestDTO accountReq);
    List<TransactionResumeResponseDTO> getTransactions(Long accountId);
    List<ReservationResponseDto> getReservations(Long accountId);
    List<String> getCurrencies();
}

package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    List<AccountResponseDTO> getAccountsByUser();
    AccountResponseDTO createAccount(@Valid AccountRequestDTO accountReq);
    //CheckAccountResponseDTO checkAccount(CheckAccountRequestDto chkAcc);

    //List<TransactionResumeResponseDTO> getTransactions(Long accountId);
    //List<ReservationResponseDto> getReservations(Long accountId);
    List<String> getCurrencies();
    List<AccountResponseDTO> getAccountsByUserAll();
    TransactionResponseDTO transfer(Long sourceAccountId, TransferRequestDTO transferRequestDTO);
    TransactionResponseDTO addFundsFromCard(Long sourceAccountId, @Valid DepositRequestDTO depositRequestDTO);
}

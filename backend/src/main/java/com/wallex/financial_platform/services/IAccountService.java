package com.wallex.financial_platform.services;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    List<AccountResponseDTO> getAccountsByUser();
    AccountResponseDTO createAccount(@Valid AccountRequestDTO accountReq);
    List<String> getCurrencies();
    List<AccountResponseDTO> getAccountsByUserAll();
    TransactionResponseDTO transfer(Long sourceAccountId, TransferRequestDTO transferRequestDTO);
    TransactionResponseDTO addFundsFromCard(Long sourceAccountId, @Valid DepositRequestDTO depositRequestDTO);
    TransactionResponseDTO createReservation(Long sourceAccountId, @Valid ReservationRequestDTO reservationRequestDTO);
    TransactionResponseDTO releaseReservation(Long reservationId, Long accountId);
    ValidateTransferResultResponseDTO  validateAccountIdentifier(String destination);

    BalanceCheckResponseDTO checkAccountBalance(Long accountId, BigDecimal amount);
}

package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {
    TransactionResponseDTO createTransferTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason);
    TransactionResponseDTO createDepositTransaction(Account account, BigDecimal amount, String cardNumber);
    List<TransactionResponseDTO> getTransactionByAccount(Long id);

    TransactionResponseDTO createReservationTransaction(Account account, ReservationRequestDTO reservationRequestDTO);

    TransactionResponseDTO releaseReservationTransaction(Account account, ReservationResponseDTO reservationResponseDTO);

    TransactionResponseDTO createYieldTransaction(Account accountWallexPesos, Account account, BigDecimal profit, String rendimiento, TransactionType transactionType);
}

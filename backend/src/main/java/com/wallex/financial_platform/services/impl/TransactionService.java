package com.wallex.financial_platform.services.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import com.wallex.financial_platform.dtos.requests.TransactionRequestDTO;
import com.wallex.financial_platform.dtos.responses.CheckAccountResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.TransactionErrorException;
import com.wallex.financial_platform.exceptions.TransactionNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.services.ITransactionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.repositories.TransactionRepository;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserContextService userContextService;
    private MovementService movementService;

    @Override
    @SneakyThrows
    public TransactionResponseDTO getById(Long transactionId) {
        User user = this.userContextService.getAuthenticatedUser();
        Transaction foundTransaction = transactionRepository.findByTransactionIdAndUser(transactionId, user.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return mapToDTO(foundTransaction);
    }

    @Override
    @SneakyThrows
    public TransactionResponseDTO save(TransactionRequestDTO transactionReq) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() == "anonymousUser") {
            throw new TransactionErrorException("User not authenticated");
        }
        Account sourceAccount = accountRepository.findByCbuOrAlias(transactionReq.sourceCbu(), transactionReq.sourceCbu())
                .orElseThrow(()-> new AccountNotFoundException("Source Account not found"));
        if (!Objects.equals(sourceAccount.getUser().getEmail(), auth.getPrincipal().toString())) {
            throw new TransactionErrorException("User not authorized to perform this transaction");
        }
        Account destinationAccount = accountRepository.findByCbuOrAlias(transactionReq.destinationCbu(), transactionReq.destinationCbu())
                .orElseThrow(()-> new AccountNotFoundException("Destination Account not found"));
        if (sourceAccount.getCurrency() != destinationAccount.getCurrency()) {
            throw new TransactionErrorException("Currency mismatch");
        }

        if (sourceAccount.getAvailableBalance().compareTo(transactionReq.amount()) < 0) {
            throw new TransactionErrorException("Insufficient funds");
        }
        Transaction transaction = mapToEntity(transactionReq);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        if (transactionReq.type() == TransactionType.TRANSFER) {
            transaction.setStatus(TransactionStatus.COMPLETED);
        } else {
            transaction.setStatus(TransactionStatus.PENDING);
        }
        Transaction newTransaction = transactionRepository.save(transaction);
        movementService.save(newTransaction);
        return mapToDTO(newTransaction);
    }

    public TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                mapToDTO(transaction.getSourceAccount()),
                mapToDTO(transaction.getDestinationAccount()),
                transaction.getTransactionDateTime(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getReason()
        );
    }
    private CheckAccountResponseDTO mapToDTO(Account account) {
        return new CheckAccountResponseDTO(
                account.getCbu(),
                account.getAlias(),
                account.getCurrency(),
                account.getUser().getFullName(),
                account.getUser().getDni()
        );
    }

    private Transaction mapToEntity(TransactionRequestDTO transaction) {
        return Transaction.builder()
                .reason(transaction.reason())
                .amount(transaction.amount())
                .type(transaction.type())
                .transactionDateTime(LocalDateTime.now())
                .build();
    }
}

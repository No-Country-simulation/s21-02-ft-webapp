package com.wallex.financial_platform.services.impl;

import java.util.List;
import java.util.Optional;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.exceptions.TransactionNotFoundException;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.ITransactionService;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.repositories.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {
   private TransactionRepository transactionRepository;
   private UserRepository userRepository;

    @Override
    @SneakyThrows
    public TransactionResponseDTO getById(Long transactionId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() == "anonymousUser") {
            throw new Exception("User not authenticated");
        }
        User user = userRepository.findByEmail(auth.getPrincipal().toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction foundTransaction = transactionRepository.findByTransactionIdAndUser(transactionId, user.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return mapToDTO(foundTransaction);
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionDateTime(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getReason()
        );
    }
}

package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDataLoader {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void load() {
        Account account1 = accountRepository.findById(1L).orElseThrow();
        Account account2 = accountRepository.findById(2L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();
        Account account4 = accountRepository.findById(4L).orElseThrow();

        // Crear transacciones con datos realistas
        List<Transaction> transactions = List.of(
                new Transaction(null, account2, account3, new BigDecimal("150000.00").negate(), TransactionType.TRANSFER, "Pago de alquiler", LocalDateTime.now(), TransactionStatus.COMPLETED, new ArrayList<>()),
                new Transaction(null, account2, account4, new BigDecimal("100000.00").negate(), TransactionType.TRANSFER, "Pago por prestamo", LocalDateTime.now(), TransactionStatus.COMPLETED,new ArrayList<>()),
                new Transaction(null, account2, account2, new BigDecimal("20000.00"), TransactionType.DEPOSIT, "Ingreso de dinero desde tarjeta de debito", LocalDateTime.now(), TransactionStatus.COMPLETED, new ArrayList<>())

        );

        // Guardar las transacciones en el repositorio
        transactionRepository.saveAll(transactions);
    }
}

package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        Account account5 = accountRepository.findById(5L).orElseThrow();
        Account account6 = accountRepository.findById(6L).orElseThrow();

        // Fechas hardcodeadas
        LocalDateTime date1 = LocalDateTime.of(2025, 2, 21, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 2, 22, 12, 30);
        LocalDateTime date3 = LocalDateTime.of(2025, 2, 23, 15, 45);
        LocalDateTime date4 = LocalDateTime.of(2025, 2, 24, 9, 15);
        LocalDateTime date5 = LocalDateTime.of(2025, 2, 25, 14, 0);
        LocalDateTime date6 = LocalDateTime.of(2025, 2, 26, 11, 30);
        LocalDateTime date7 = LocalDateTime.of(2025, 2, 27, 16, 45);
        LocalDateTime date8 = LocalDateTime.of(2025, 3, 1, 8, 0);
        LocalDateTime date9 = LocalDateTime.of(2025, 3, 1, 13, 15);
        LocalDateTime date10 = LocalDateTime.of(2025, 3, 2, 17, 30);
        LocalDateTime date11 = LocalDateTime.of(2025, 3, 2, 10, 45);
        LocalDateTime date12 = LocalDateTime.of(2025, 3, 3, 12, 0);
        LocalDateTime date13 = LocalDateTime.of(2025, 3, 3, 9, 30);
        LocalDateTime date14 = LocalDateTime.of(2025, 3, 4, 14, 15);
        LocalDateTime date15 = LocalDateTime.of(2025, 3, 5, 18, 0);
        LocalDateTime date16 = LocalDateTime.of(2025, 3, 6, 7, 45);
        LocalDateTime date17 = LocalDateTime.of(2025, 3, 7, 11, 0);
        LocalDateTime date18 = LocalDateTime.of(2025, 3, 8, 16, 30);

        List<Transaction> transactions = List.of(
                new Transaction(null, account6, account1, new BigDecimal("500000.00"), TransactionType.TRANSFER, "Ingreso de dinero desde tarjeta", date1, TransactionStatus.COMPLETED),
                new Transaction(null, account6, account2, new BigDecimal("200000.00"), TransactionType.TRANSFER, "Ingreso de dinero desde tarjeta", date2, TransactionStatus.COMPLETED),
                new Transaction(null, account6, account3, new BigDecimal("200000.00"), TransactionType.TRANSFER, "Ingreso de dinero desde tarjeta", date3, TransactionStatus.COMPLETED),
                new Transaction(null, account6, account4, new BigDecimal("1500000.00"), TransactionType.TRANSFER, "Ingreso de dinero desde tarjeta", date4, TransactionStatus.COMPLETED),

                new Transaction(null, account2, account1, new BigDecimal("10000.00"), TransactionType.DEPOSIT, "Pago de servicio", date5, TransactionStatus.COMPLETED),
                new Transaction(null, account2, account3, new BigDecimal("5000.00"), TransactionType.WITHDRAWAL, "Retiro de efectivo", date6, TransactionStatus.COMPLETED),
                new Transaction(null, account3, account1, new BigDecimal("2000.00"), TransactionType.DEPOSIT, "Depósito de salario", date7, TransactionStatus.COMPLETED),
                new Transaction(null, account1, account5, new BigDecimal("150000.00"), TransactionType.TRANSFER, "Pago de alquiler", date8, TransactionStatus.COMPLETED),
                new Transaction(null, account2, account1, new BigDecimal("3000.00"), TransactionType.DEPOSIT, "Reembolso", date9, TransactionStatus.COMPLETED),
                new Transaction(null, account3, account2, new BigDecimal("7000.00"), TransactionType.WITHDRAWAL, "Retiro bancario", date10, TransactionStatus.COMPLETED),
                new Transaction(null, account1, account2, new BigDecimal("5000.00"), TransactionType.TRANSFER, "Compra en línea", date11, TransactionStatus.COMPLETED),
                new Transaction(null, account2, account4, new BigDecimal("12000.00"), TransactionType.TRANSFER, "Pago de préstamo", date12, TransactionStatus.COMPLETED),
                new Transaction(null, account3, account1, new BigDecimal("9000.00"), TransactionType.DEPOSIT, "Pago de cliente", date13, TransactionStatus.COMPLETED),
                new Transaction(null, account1, account2, new BigDecimal("2500.00"), TransactionType.TRANSFER, "Pago de factura", date14, TransactionStatus.COMPLETED),
                new Transaction(null, account2, account3, new BigDecimal("8000.00"), TransactionType.WITHDRAWAL, "Retiro de efectivo", date15, TransactionStatus.COMPLETED),
                new Transaction(null, account3, account1, new BigDecimal("10000.00"), TransactionType.DEPOSIT, "Ingreso de intereses", date16, TransactionStatus.COMPLETED),
                new Transaction(null, account1, account3, new BigDecimal("6000.00"), TransactionType.TRANSFER, "Compra de insumos", date17, TransactionStatus.COMPLETED),
                new Transaction(null, account2, account1, new BigDecimal("4500.00"), TransactionType.DEPOSIT, "Pago de amigo", date18, TransactionStatus.COMPLETED),
                new Transaction(null, account3, account2, new BigDecimal("3000.00"), TransactionType.WITHDRAWAL, "Retiro rápido", date18, TransactionStatus.COMPLETED)
        );

        transactionRepository.saveAll(transactions);
    }
}
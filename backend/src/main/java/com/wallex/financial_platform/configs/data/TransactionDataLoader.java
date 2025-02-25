package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class TransactionDataLoader {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void load() {
        // Obtener algunas cuentas para asociar con las transacciones
        Account account1 = accountRepository.findById(1L).orElseThrow();
        Account account2 = accountRepository.findById(2L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();

        // Crear transacciones
        List<Transaction> transactions = List.of(
                new Transaction(null, account1, account2, new BigDecimal("1000.00"), TransactionType.TRANSFER, "Pago de servicio", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account2, account3, new BigDecimal("500.00"), TransactionType.WITHDRAWAL, "Retiro de efectivo", LocalDateTime.now(), TransactionStatus.PENDING),
                new Transaction(null, account3, account1, new BigDecimal("200.00"), TransactionType.DEPOSIT, "Depósito de salario", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account1, account3, new BigDecimal("150.00"), TransactionType.TRANSFER, "Pago de alquiler", LocalDateTime.now(), TransactionStatus.FAILED),
                new Transaction(null, account2, account1, new BigDecimal("300.00"), TransactionType.DEPOSIT, "Reembolso", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account3, account2, new BigDecimal("700.00"), TransactionType.WITHDRAWAL, "Retiro bancario", LocalDateTime.now(), TransactionStatus.PENDING),
                new Transaction(null, account1, account2, new BigDecimal("50.00"), TransactionType.TRANSFER, "Compra en línea", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account2, account3, new BigDecimal("1200.00"), TransactionType.TRANSFER, "Pago de préstamo", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account3, account1, new BigDecimal("900.00"), TransactionType.DEPOSIT, "Pago de cliente", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account1, account2, new BigDecimal("250.00"), TransactionType.TRANSFER, "Pago de factura", LocalDateTime.now(), TransactionStatus.FAILED),
                new Transaction(null, account2, account3, new BigDecimal("800.00"), TransactionType.WITHDRAWAL, "Retiro de efectivo", LocalDateTime.now(), TransactionStatus.PENDING),
                new Transaction(null, account3, account1, new BigDecimal("100.00"), TransactionType.DEPOSIT, "Ingreso de intereses", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account1, account3, new BigDecimal("600.00"), TransactionType.TRANSFER, "Compra de insumos", LocalDateTime.now(), TransactionStatus.PENDING),
                new Transaction(null, account2, account1, new BigDecimal("450.00"), TransactionType.DEPOSIT, "Pago de amigo", LocalDateTime.now(), TransactionStatus.COMPLETED),
                new Transaction(null, account3, account2, new BigDecimal("300.00"), TransactionType.WITHDRAWAL, "Retiro rápido", LocalDateTime.now(), TransactionStatus.PENDING)
        );

        // Guardar las transacciones en el repositorio
        transactionRepository.saveAll(transactions);
    }
}

package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDataLoader {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void load() {
        // Obtener algunas cuentas con datos de Argentina
        Account account1 = accountRepository.findById(1L).orElseThrow();
        Account account2 = accountRepository.findById(2L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();
        Account account4 = accountRepository.findById(4L).orElseThrow();
        Account account5 = accountRepository.findById(5L).orElseThrow();

        // Crear transacciones con datos realistas
        List<Transaction> transactions = List.of(
                new Transaction(null, account1, account2, new BigDecimal("150000.00"), TransactionType.TRANSFER, "Pago de alquiler", LocalDateTime.now(), TransactionStatus.COMPLETED)
               // new Transaction(null, account2, account3, new BigDecimal("85000.00"), TransactionType.TRANSFER, "Transferencia a cuenta sueldo", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account3, account4, new BigDecimal("20000.00"), TransactionType.TRANSFER, "Pago de tarjeta de crédito", LocalDateTime.now(), TransactionStatus.COMPLETED),
                //new Transaction(null, account4, account5, new BigDecimal("60000.00"), TransactionType.TRANSFER, "Compra de insumos para la empresa", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account5, account1, new BigDecimal("35000.00"), TransactionType.TRANSFER, "Pago de proveedores", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account1, account3, new BigDecimal("5000.00"), TransactionType.TRANSFER, "Transferencia a cuenta ahorro", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account2, account4, new BigDecimal("70000.00"), TransactionType.TRANSFER, "Pago de impuestos AFIP", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account3, account5, new BigDecimal("25000.00"), TransactionType.TRANSFER, "Pago de honorarios", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account4, account1, new BigDecimal("40000.00"), TransactionType.TRANSFER, "Inversión en plazo fijo", LocalDateTime.now(), TransactionStatus.COMPLETED),
               // new Transaction(null, account5, account2, new BigDecimal("10000.00"), TransactionType.TRANSFER, "Gasto en publicidad", LocalDateTime.now(), TransactionStatus.COMPLETED)
        );

        // Guardar las transacciones en el repositorio
        transactionRepository.saveAll(transactions);
    }
}

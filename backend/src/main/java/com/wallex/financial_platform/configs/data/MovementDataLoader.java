package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class MovementDataLoader {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void load() {
        // Obtener las transacciones registradas
        List<Transaction> transactions = transactionRepository.findAll();

        // Crear movimientos basados en las transacciones
        List<Movement> movements = transactions.stream().map(transaction -> {
            Account account = transaction.getSourceAccount();
            return new Movement(
                    null,
                    account,
                    transaction,
                    "Movimiento generado para la transacción " + transaction.getTransactionId(),
                    transaction.getAmount(),
                    LocalDateTime.now()
            );
        }).toList();

        // Guardar los movimientos en el repositorio
        movementRepository.saveAll(movements);
    }
}

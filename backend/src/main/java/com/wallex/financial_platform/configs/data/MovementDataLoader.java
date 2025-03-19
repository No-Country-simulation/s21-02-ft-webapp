package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovementDataLoader {

    private final MovementRepository movementRepository;
    private final TransactionRepository transactionRepository;

    public void load() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Movement> movements = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Account sourceAccount = transaction.getSourceAccount();
            Account destinationAccount = transaction.getDestinationAccount();

            if (sourceAccount.getAccountId().equals(destinationAccount.getAccountId())
                    && transaction.getType() == TransactionType.DEPOSIT || transaction.getType() == TransactionType.RENDIMIENTO || transaction.getType() == TransactionType.RESERVE) {
                // Caso de depósito: solo un movimiento de entrada
                Movement depositMovement = new Movement(
                        null,
                        sourceAccount,
                        transaction,
                        "Depósito desde fuente externa",
                        transaction.getAmount(), // Monto positivo
                        LocalDateTime.now()
                );
                movements.add(depositMovement);
            } else {
                Movement debitMovement = new Movement(
                        null,
                        sourceAccount,
                        transaction,
                        "Transferencia enviada a " + destinationAccount.getAccountId(),
                        transaction.getAmount(), // Monto negativo
                        LocalDateTime.now()
                );

                Movement creditMovement = new Movement(
                        null,
                        destinationAccount,
                        transaction,
                        "Transferencia recibida de " + sourceAccount.getAccountId(),
                        transaction.getAmount().negate(), // Monto positivo
                        LocalDateTime.now()
                );

                movements.add(debitMovement);
                movements.add(creditMovement);
            }
        }

        movementRepository.saveAll(movements);
    }
}
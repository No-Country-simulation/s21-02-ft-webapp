package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.exceptions.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.AllArgsConstructor;
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
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    public void load() {
        // Obtener las transacciones registradas
        List<Transaction> transactions = transactionRepository.findAll();

        // Crear movimientos basados en las transacciones
        List<Movement> movements = transactions.stream().map(transaction -> {
            Account account = transaction.getSourceAccount();
            User user = accountRepository.findById(account.getAccountId()).orElseThrow().getUser();
            return new Movement(
                    null,
                    user,
                    transaction,
                    "Movimiento generado para la transacción " + transaction.getTransactionId(),
                    transaction.getAmount(),
                    LocalDateTime.now()
            );
        }).toList();

        // Guardar los movimientos en el repositorio
        List<Movement> movementsList = movementRepository.saveAll(movements);

    }
}

package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional // ← MOVIDO A NIVEL DE CLASE para mantener sesión en TODOS los métodos
public class MovementDataLoader {

    private final MovementRepository movementRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void load() {
        // Verificar si ya existen movimientos
        if (movementRepository.count() > 0) {
            System.out.println("📝 Ya existen movimientos, omitiendo creación");
            return;
        }

        List<Transaction> transactions = loadTransactionsSafely();

        if (transactions.isEmpty()) {
            System.out.println("⚠️ No hay transacciones para crear movimientos");
            return;
        }

        List<Movement> movements = new ArrayList<>();
        int movementsCreated = 0;

        for (Transaction transaction : transactions) {
            List<Movement> transactionMovements = createMovementsSafe(transaction);
            movements.addAll(transactionMovements);
            movementsCreated += transactionMovements.size();
        }

        if (!movements.isEmpty()) {
            movementRepository.saveAll(movements);
            System.out.println("✅ " + movementsCreated + " movimientos creados para " +
                    transactions.size() + " transacciones");
        }
    }

    /**
     * Carga transacciones de forma segura, evitando proxies no inicializados
     */
    private List<Transaction> loadTransactionsSafely() {
        try {
            // Intentar método optimizado si existe
            return transactionRepository.findAllWithAccounts();
        } catch (Exception e) {
            // Fallback: cargar y reemplazar proxies
            return loadAndFixTransactionProxies();
        }
    }

    /**
     * Carga transacciones y reemplaza proxies por entidades reales
     */
    private List<Transaction> loadAndFixTransactionProxies() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> fixedTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            try {
                // Crear una copia "segura" de la transacción con cuentas reales
                Transaction safeTransaction = createSafeTransactionCopy(transaction);
                fixedTransactions.add(safeTransaction);
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo procesar transacción " +
                        transaction.getTransactionId() + ": " + e.getMessage());
            }
        }

        return fixedTransactions;
    }

    /**
     * Crea una copia segura de la transacción con cuentas cargadas explícitamente
     */
    private Transaction createSafeTransactionCopy(Transaction original) {
        // Cargar cuenta de origen COMPLETA desde la BD
        Account sourceAccount = accountRepository.findById(
                original.getSourceAccount().getAccountId()
        ).orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));

        // Cargar cuenta de destino (si existe)
        Account destinationAccount = null;
        if (original.getDestinationAccount() != null) {
            destinationAccount = accountRepository.findById(
                    original.getDestinationAccount().getAccountId()
            ).orElse(null);
        }

        // Crear nueva transacción (o modificar la existente) con cuentas reales
        Transaction safeTransaction = new Transaction();
        safeTransaction.setTransactionId(original.getTransactionId());
        safeTransaction.setSourceAccount(sourceAccount);
        safeTransaction.setDestinationAccount(destinationAccount);
        safeTransaction.setAmount(original.getAmount());
        safeTransaction.setType(original.getType());
        safeTransaction.setReason(original.getReason());
        safeTransaction.setTransactionDate(original.getTransactionDate());
        safeTransaction.setStatus(original.getStatus());

        return safeTransaction;
    }

    /**
     * Versión SEGURA que NO usa account.getAlias()
     */
    private List<Movement> createMovementsSafe(Transaction transaction) {
        List<Movement> movements = new ArrayList<>();

        Account sourceAccount = transaction.getSourceAccount();
        Account destinationAccount = transaction.getDestinationAccount();
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();

        if (type == TransactionType.DEPOSIT ||
                type == TransactionType.RENDIMIENTO ||
                type == TransactionType.RESERVE) {

            // Movimiento único de entrada
            Movement movement = new Movement(
                    null,
                    sourceAccount,
                    transaction,
                    getSimpleDescription(type, transaction.getReason()),
                    amount.abs(),
                    LocalDateTime.now()
            );
            movements.add(movement);

        } else if (type == TransactionType.TRANSFER && destinationAccount != null) {

            // Verificar si es transferencia a la misma cuenta
            boolean sameAccount = sourceAccount.getAccountId().equals(destinationAccount.getAccountId());

            if (sameAccount) {
                // Transferencia interna
                Movement movement = new Movement(
                        null,
                        sourceAccount,
                        transaction,
                        "Transferencia interna",
                        amount.abs(),
                        LocalDateTime.now()
                );
                movements.add(movement);
            } else {
                // Usar IDs en lugar de alias (EVITA el problema)
                String sourceId = "Cuenta #" + sourceAccount.getAccountId();
                String destId = "Cuenta #" + destinationAccount.getAccountId();

                // Movimiento de débito
                Movement debitMovement = new Movement(
                        null,
                        sourceAccount,
                        transaction,
                        "Transferencia a " + destId,
                        amount.negate(),
                        LocalDateTime.now()
                );

                // Movimiento de crédito
                Movement creditMovement = new Movement(
                        null,
                        destinationAccount,
                        transaction,
                        "Transferencia desde " + sourceId,
                        amount.abs(),
                        LocalDateTime.now()
                );

                movements.add(debitMovement);
                movements.add(creditMovement);
            }
        } else {
            // Otros tipos
            Movement movement = new Movement(
                    null,
                    sourceAccount,
                    transaction,
                    getSimpleDescription(type, transaction.getReason()),
                    amount,
                    LocalDateTime.now()
            );
            movements.add(movement);
        }

        return movements;
    }

    /**
     * Descripción simple SIN acceder a propiedades lazy
     */
    private String getSimpleDescription(TransactionType type, String reason) {
        String base = switch (type) {
            case DEPOSIT -> "Depósito";
            case TRANSFER -> "Transferencia";
            case RENDIMIENTO -> "Rendimiento";
            case RESERVE -> "Reserva";
            default -> "Movimiento";
        };

        return (reason != null && !reason.isEmpty())
                ? base + ": " + reason
                : base;
    }

    /**
     * Método OPCIONAL si en el futuro quieres usar alias
     * Versión segura que maneja proxies
     */
    private String getAccountIdentifierSafe(Account account) {
        if (account == null) {
            return "Cuenta no disponible";
        }

        // PRIMERO intentar obtener el ID (siempre funciona)
        Long accountId = account.getAccountId();

        // LUEGO intentar alias de forma segura
        try {
            String alias = account.getAlias();
            if (alias != null && !alias.trim().isEmpty()) {
                return alias;
            }
        } catch (Exception e) {
            // Si falla, es un proxy no inicializado
            // No hacemos nada, usaremos el ID
        }

        // Fallback al ID
        return "Cuenta #" + accountId;
    }
}
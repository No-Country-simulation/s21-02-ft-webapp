package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository; // ← NUEVO
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import jakarta.persistence.EntityManager; // ← NUEVO (opcional, para la solución 2)
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate; // ← NUEVO
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovementDataLoader {

    private final MovementRepository movementRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository; // ← INYECTAR AccountRepository

    @Transactional
    public void load() {
        // Verificar si ya existen movimientos
        long existingMovements = movementRepository.count();
        if (existingMovements > 0) {
            System.out.println("📝 Ya existen " + existingMovements + " movimientos, omitiendo creación");
            return;
        }

        // Cargar transacciones con sus cuentas inicializadas
        List<Transaction> transactions = transactionRepository.findAllWithAccounts(); // ← Método nuevo

        // Si no tienes el método findAllWithAccounts, usa este:
        // List<Transaction> transactions = loadTransactionsWithAccounts();

        if (transactions.isEmpty()) {
            System.out.println("⚠️ No hay transacciones para crear movimientos");
            return;
        }

        List<Movement> movements = new ArrayList<>();
        int movementsCreated = 0;

        for (Transaction transaction : transactions) {
            // Inicializar proxies ANTES de usarlos (solución alternativa)
            initializeAccountProxy(transaction.getSourceAccount());
            if (transaction.getDestinationAccount() != null) {
                initializeAccountProxy(transaction.getDestinationAccount());
            }

            // Validar que la transacción tiene cuentas válidas
            if (!isValidTransactionForMovements(transaction)) {
                System.out.println("⚠️ Transacción " + transaction.getTransactionId() +
                        " ignorada por datos inválidos");
                continue;
            }

            List<Movement> transactionMovements = createMovementsForTransaction(transaction);
            movements.addAll(transactionMovements);
            movementsCreated += transactionMovements.size();
        }

        if (!movements.isEmpty()) {
            try {
                movementRepository.saveAll(movements);
                System.out.println("✅ " + movementsCreated + " movimientos creados para " +
                        transactions.size() + " transacciones");
            } catch (Exception e) {
                System.err.println("❌ Error al guardar movimientos: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No se crearon movimientos (transacciones inválidas)");
        }
    }

    /**
     * Método alternativo si no puedes modificar TransactionRepository
     */
    private List<Transaction> loadTransactionsWithAccounts() {
        List<Transaction> transactions = transactionRepository.findAll();

        // Para cada transacción, cargar las cuentas completas desde la BD
        for (Transaction transaction : transactions) {
            // Reemplazar proxies por entidades reales
            Account sourceAccount = accountRepository.findById(
                    transaction.getSourceAccount().getAccountId()
            ).orElse(null);

            if (sourceAccount != null) {
                // Usar reflexión o setter para reemplazar la cuenta
                transaction.setSourceAccount(sourceAccount);
            }

            if (transaction.getDestinationAccount() != null) {
                Account destAccount = accountRepository.findById(
                        transaction.getDestinationAccount().getAccountId()
                ).orElse(null);

                if (destAccount != null) {
                    transaction.setDestinationAccount(destAccount);
                }
            }
        }

        return transactions;
    }

    /**
     * Inicializa un proxy de Hibernate si es necesario
     */
    private void initializeAccountProxy(Account account) {
        if (account != null) {
            // Esto fuerza la inicialización del proxy
            Hibernate.initialize(account);
            // También puedes inicializar propiedades específicas
            Hibernate.initialize(account.getAlias());
        }
    }

    private boolean isValidTransactionForMovements(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        Account sourceAccount = transaction.getSourceAccount();

        if (sourceAccount == null) {
            return false;
        }

        // Para depósitos, rendimientos y reservas, solo necesitamos cuenta de origen
        if (transaction.getType() == TransactionType.DEPOSIT ||
                transaction.getType() == TransactionType.RENDIMIENTO ||
                transaction.getType() == TransactionType.RESERVE) {
            return true;
        }

        // Para transferencias, necesitamos ambas cuentas
        return transaction.getDestinationAccount() != null;
    }

    private List<Movement> createMovementsForTransaction(Transaction transaction) {
        List<Movement> movements = new ArrayList<>();

        Account sourceAccount = transaction.getSourceAccount();
        Account destinationAccount = transaction.getDestinationAccount();
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();

        // Determinar descripción según tipo de transacción
        String description = getMovementDescription(transaction, type);

        if (type == TransactionType.DEPOSIT ||
                type == TransactionType.RENDIMIENTO ||
                type == TransactionType.RESERVE) {

            // Movimiento único de entrada
            Movement movement = new Movement(
                    null,
                    sourceAccount,
                    transaction,
                    description,
                    amount.abs(), // Monto positivo para entrada
                    LocalDateTime.now()
            );
            movements.add(movement);

        } else if (type == TransactionType.TRANSFER && destinationAccount != null) {

            // Verificar si es transferencia a la misma cuenta
            boolean sameAccount = sourceAccount.getAccountId().equals(destinationAccount.getAccountId());

            if (sameAccount) {
                // Transferencia interna - movimiento único
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
                // CORRECCIÓN: Asegurar que las cuentas están inicializadas
                String destIdentifier = getAccountIdentifier(destinationAccount);
                String sourceIdentifier = getAccountIdentifier(sourceAccount);

                // Dos movimientos: débito y crédito
                Movement debitMovement = new Movement(
                        null,
                        sourceAccount,
                        transaction,
                        "Transferencia enviada a cuenta " + destIdentifier,
                        amount.negate(), // Simplificado: ya es negativo
                        LocalDateTime.now()
                );

                Movement creditMovement = new Movement(
                        null,
                        destinationAccount,
                        transaction,
                        "Transferencia recibida de cuenta " + sourceIdentifier,
                        amount.abs(), // Monto positivo para entrada
                        LocalDateTime.now()
                );

                movements.add(debitMovement);
                movements.add(creditMovement);
            }
        } else {
            // Otros tipos de transacción - movimiento simple
            Movement movement = new Movement(
                    null,
                    sourceAccount,
                    transaction,
                    description,
                    amount,
                    LocalDateTime.now()
            );
            movements.add(movement);
        }

        return movements;
    }

    private String getMovementDescription(Transaction transaction, TransactionType type) {
        String baseDescription = transaction.getReason() != null ?
                transaction.getReason() : "";

        return switch (type) {
            case DEPOSIT -> "Depósito" + (baseDescription.isEmpty() ? "" : ": " + baseDescription);
            case TRANSFER -> "Transferencia" + (baseDescription.isEmpty() ? "" : ": " + baseDescription);
            case RENDIMIENTO -> "Rendimiento" + (baseDescription.isEmpty() ? "" : ": " + baseDescription);
            case RESERVE -> "Reserva" + (baseDescription.isEmpty() ? "" : ": " + baseDescription);
            default -> "Movimiento" + (baseDescription.isEmpty() ? "" : ": " + baseDescription);
        };
    }

    private String getAccountIdentifier(Account account) {
        // Asegurar que el proxy está inicializado
        if (account == null) {
            return "Cuenta no disponible";
        }

        // Inicializar proxy si es necesario
        initializeAccountProxy(account);

        if (account.getAlias() != null && !account.getAlias().isEmpty()) {
            return account.getAlias();
        } else if (account.getCbu() != null) {
            // Mostrar solo últimos 4 dígitos del CBU por seguridad
            String cbu = account.getCbu();
            return cbu.length() > 4 ? "****" + cbu.substring(cbu.length() - 4) : cbu;
        } else {
            return account.getAccountId().toString();
        }
    }
}
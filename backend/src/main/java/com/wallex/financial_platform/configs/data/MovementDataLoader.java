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
@Transactional // IMPORTANTE: Mantiene la sesión de Hibernate abierta
public class MovementDataLoader {

    private final MovementRepository movementRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * Método principal que carga los movimientos para las transacciones existentes
     * Versión segura que maneja proxies Hibernate correctamente
     */
    public void load() {
        // 1. Verificar si ya existen movimientos
        if (movementRepository.count() > 0) {
            System.out.println("📝 Ya existen movimientos, omitiendo creación");
            return;
        }

        // 2. Cargar transacciones de forma segura (evitando proxies)
        List<Transaction> transactions = loadTransactionsWithRealAccounts();

        if (transactions.isEmpty()) {
            System.out.println("⚠️ No hay transacciones para crear movimientos");
            return;
        }

        // 3. Crear movimientos para cada transacción
        List<Movement> movements = new ArrayList<>();
        int movementsCreated = 0;

        for (Transaction transaction : transactions) {
            try {
                List<Movement> transactionMovements = createMovementsForTransaction(transaction);
                movements.addAll(transactionMovements);
                movementsCreated += transactionMovements.size();
            } catch (Exception e) {
                System.err.println("⚠️ Error procesando transacción " +
                        transaction.getTransactionId() + ": " + e.getMessage());
            }
        }

        // 4. Guardar todos los movimientos creados
        if (!movements.isEmpty()) {
            movementRepository.saveAll(movements);
            System.out.println("✅ " + movementsCreated + " movimientos creados exitosamente");
        } else {
            System.out.println("⚠️ No se crearon movimientos");
        }
    }

    /**
     * Carga transacciones asegurando que las cuentas sean entidades REALES, no proxies
     */
    private List<Transaction> loadTransactionsWithRealAccounts() {
        try {
            // Intento 1: Usar método optimizado con JOIN FETCH si existe
            return transactionRepository.findAllWithAccounts();
        } catch (Exception e) {
            System.out.println("ℹ️ Usando carga alternativa de transacciones...");

            // Intento 2: Cargar normalmente y luego reemplazar proxies
            List<Transaction> rawTransactions = transactionRepository.findAll();
            List<Transaction> safeTransactions = new ArrayList<>();

            for (Transaction transaction : rawTransactions) {
                Transaction safeTransaction = replaceAccountProxies(transaction);
                safeTransactions.add(safeTransaction);
            }

            return safeTransactions;
        }
    }

    /**
     * Reemplaza las cuentas proxy por entidades reales cargadas desde la BD
     */
    private Transaction replaceAccountProxies(Transaction transaction) {
        // Crear una nueva instancia o clonar la transacción
        Transaction safeTransaction = new Transaction();

        // Copiar todos los atributos básicos
        safeTransaction.setTransactionId(transaction.getTransactionId());
        safeTransaction.setAmount(transaction.getAmount());
        safeTransaction.setType(transaction.getType());
        safeTransaction.setReason(transaction.getReason());
        safeTransaction.setTransactionDateTime(transaction.getTransactionDateTime());
        safeTransaction.setStatus(transaction.getStatus());

        // Cargar cuenta de origen REAL desde la BD
        Long sourceAccountId = transaction.getSourceAccount().getAccountId();
        Account realSourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada: " + sourceAccountId));
        safeTransaction.setSourceAccount(realSourceAccount);

        // Cargar cuenta de destino REAL si existe
        if (transaction.getDestinationAccount() != null) {
            Long destAccountId = transaction.getDestinationAccount().getAccountId();
            Account realDestAccount = accountRepository.findById(destAccountId)
                    .orElse(null);
            safeTransaction.setDestinationAccount(realDestAccount);
        }

        return safeTransaction;
    }

    /**
     * Crea movimientos para una transacción específica
     * Versión segura que NO accede a propiedades lazy (como getAlias())
     */
    private List<Movement> createMovementsForTransaction(Transaction transaction) {
        List<Movement> movements = new ArrayList<>();

        Account sourceAccount = transaction.getSourceAccount();
        Account destinationAccount = transaction.getDestinationAccount();
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();
        String reason = transaction.getReason();

        switch (type) {
            case DEPOSIT:
            case RENDIMIENTO:
            case RESERVE:
                // Movimiento único de ENTRADA
                movements.add(createMovement(
                        sourceAccount,
                        transaction,
                        generateDescription(type, reason, null, false),
                        amount.abs(), // Monto positivo
                        LocalDateTime.now()
                ));
                break;

            case TRANSFER:
                if (destinationAccount != null) {
                    boolean sameAccount = sourceAccount.getAccountId().equals(destinationAccount.getAccountId());

                    if (sameAccount) {
                        // Transferencia interna (misma cuenta)
                        movements.add(createMovement(
                                sourceAccount,
                                transaction,
                                "Transferencia interna",
                                amount.abs(),
                                LocalDateTime.now()
                        ));
                    } else {
                        // Transferencia entre cuentas diferentes: 2 movimientos

                        // 1. Movimiento de DÉBITO (salida) en cuenta origen
                        movements.add(createMovement(
                                sourceAccount,
                                transaction,
                                generateDescription(type, reason, destinationAccount, true),
                                amount.negate(), // Monto negativo
                                LocalDateTime.now()
                        ));

                        // 2. Movimiento de CRÉDITO (entrada) en cuenta destino
                        movements.add(createMovement(
                                destinationAccount,
                                transaction,
                                generateDescription(type, reason, sourceAccount, false),
                                amount.abs(), // Monto positivo
                                LocalDateTime.now()
                        ));
                    }
                }
                break;

            default:
                // Para cualquier otro tipo de transacción
                movements.add(createMovement(
                        sourceAccount,
                        transaction,
                        generateDescription(type, reason, null, false),
                        amount,
                        LocalDateTime.now()
                ));
        }

        return movements;
    }

    /**
     * Crea un movimiento individual
     */
    private Movement createMovement(Account account, Transaction transaction,
                                    String description, BigDecimal amount, LocalDateTime date) {
        return new Movement(
                null,           // ID generado automáticamente
                account,        // Cuenta asociada
                transaction,    // Transacción relacionada
                description,    // Descripción generada
                amount,         // Monto (positivo=entrada, negativo=salida)
                date            // Fecha del movimiento
        );
    }

    /**
     * Genera descripciones seguras SIN acceder a propiedades lazy como getAlias()
     */
    private String generateDescription(TransactionType type, String reason,
                                       Account relatedAccount, boolean isOutgoing) {

        String baseDescription = getBaseDescription(type);
        String direction = isOutgoing ? "enviada a " : "recibida de ";

        if (relatedAccount != null) {
            // Usar solo el ID de la cuenta (evita getAlias() que causa el error)
            String accountReference = "cuenta #" + relatedAccount.getAccountId();
            baseDescription += " " + direction + accountReference;
        }

        if (reason != null && !reason.trim().isEmpty()) {
            baseDescription += ": " + reason;
        }

        return baseDescription.trim();
    }

    /**
     * Obtiene la descripción base según el tipo de transacción
     */
    private String getBaseDescription(TransactionType type) {
        return switch (type) {
            case DEPOSIT -> "Depósito";
            case TRANSFER -> "Transferencia";
            case RENDIMIENTO -> "Rendimiento";
            case RESERVE -> "Reserva";
            default -> "Movimiento";
        };
    }
}
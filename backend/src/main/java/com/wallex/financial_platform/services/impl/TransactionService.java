package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.MovementRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.services.ITransactionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wallex.financial_platform.services.utils.AccountContextService.CBU_WALLEX;

@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final MovementService movementService;
    private final NotificationService notificationService;
    private final UserContextService userContextService;

    @Override
    @Transactional
    public TransactionResponseDTO createTransferTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason) {
        String nameTransaction = this.getNameTransaction(sourceAccount);
        Transaction transaction = saveTransaction(sourceAccount, destinationAccount, amount, reason, TransactionType.valueOf(nameTransaction));
        createTransferMovements(sourceAccount, destinationAccount, amount, transaction);
        return mapToDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO createDepositTransaction(Account account, BigDecimal amount, String cardNumber) {
        Transaction transaction = this.saveTransaction(account, account, amount, "Ingreso de fondos desde tarjeta " + cardNumber, TransactionType.DEPOSIT);
        createDepositMovement(account, amount, transaction);

        notificationService.notifyUser(
                account.getUser(),
                "💰 Depósito realizado con éxito",
                "🎉 Has depositado " + amount + " " + account.getCurrency() + " en tu cuenta desde la tarjeta " + cardNumber + "."
        );
        return mapToDTO(transaction);
    }

    @Override
    public TransactionResponseDTO createReservationTransaction(Account account, ReservationRequestDTO reservationRequestDTO) {
        Transaction transaction = this.saveTransaction(account,account,reservationRequestDTO.reservedAmount(), "Reservo dinero para "+ reservationRequestDTO.type().name(), TransactionType.RESERVE);
        createReservationMovement(account,reservationRequestDTO.reservedAmount(),transaction);

        notificationService.notifyUser(
                account.getUser(),
                "💰 La reserva se realizado con éxito",
                "🎉 Has reservado " + reservationRequestDTO.reservedAmount() + " " + account.getCurrency() + " para " + reservationRequestDTO.type().name() + "."
        );
        return mapToDTO(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionByAccount(Long accountId) {

        User user = userContextService.getAuthenticatedUser();

        if (user.getAccounts().stream().noneMatch(a -> a.getAccountId().equals(accountId))) {
            throw new AccountErrorException("No tienes acceso a esta cuenta");
        }

        List<Transaction> transactions = transactionRepository.findBySourceAccountAccountIdOrDestinationAccountAccountId(accountId, accountId);

        return transactions.stream()
                .map(transaction -> {
                    BigDecimal amount = transaction.getAmount();
                    if (transaction.getSourceAccount().getAccountId().equals(accountId) && transaction.getType().equals(TransactionType.TRANSFER) ) {
                        amount = amount.negate();
                    }
                    return mapToDTO1(transaction, amount);
                })
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO mapToDTO1(Transaction transaction, BigDecimal amount) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionDateTime(),
                transaction.getSourceAccount().getAccountId(),
                transaction.getDestinationAccount().getAccountId(),
                amount,
                transaction.getReason(),
                transaction.getType()
        );
    }

    private String getNameTransaction(Account sourceAccount) {
        return sourceAccount.getCbu().equals(CBU_WALLEX)? TransactionType.RENDIMIENTO.name() : TransactionType.DEPOSIT.name();
    }

    private Transaction saveTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason, TransactionType transactionType) {
        Transaction transaction = new Transaction(null, sourceAccount, destinationAccount, amount, transactionType, reason, null, TransactionStatus.COMPLETED, new ArrayList<>());
        return transactionRepository.save(transaction);
    }

    private void createTransferMovements(Account sourceAccount, Account destinationAccount, BigDecimal amount, Transaction transaction) {
        createMovement(sourceAccount, transaction, "Transferencia enviada", amount.negate());
        createMovement(destinationAccount, transaction, "Transferencia recibida", amount);
        
        notificationService.notifyUser(
                sourceAccount.getUser(),
                "💸 Transferencia enviada",
                "📤 Has transferido " + amount + " " + sourceAccount.getCurrency() + " a la cuenta de " + destinationAccount.getUser().getFullName() + "."
        );

        // Notificar al destinatario
        notificationService.notifyUser(
                destinationAccount.getUser(),
                "💸 Transferencia recibida",
                "📥 Has recibido " + amount + " " + destinationAccount.getCurrency() + " de " + sourceAccount.getUser().getFullName() + "."
        );
    }

    private void createDepositMovement(Account account, BigDecimal amount, Transaction transaction) {
        createMovement(account, transaction, "Depósito desde tarjeta", amount);
    }

    private void createReservationMovement(Account account, BigDecimal amount, Transaction transaction) {
        createMovement(account, transaction, "Se genero reserva", amount);
    }

    private void createMovement(Account account, Transaction transaction, String description, BigDecimal amount) {
        MovementRequestDTO movementRequest = new MovementRequestDTO(
                account.getAccountId(),
                transaction.getTransactionId(),
                description,
                amount,
                LocalDateTime.now()
        );
        movementService.createMovement(movementRequest);
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionDateTime(),
                transaction.getSourceAccount().getAccountId(),
                transaction.getDestinationAccount().getAccountId(),
                transaction.getAmount(),
                transaction.getReason(),
                transaction.getType()
        );
    }
}

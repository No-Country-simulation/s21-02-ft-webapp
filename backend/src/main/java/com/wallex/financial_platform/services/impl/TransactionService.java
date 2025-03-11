package com.wallex.financial_platform.services.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import com.wallex.financial_platform.dtos.requests.CardTransactionRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransactionRequestDTO;
import com.wallex.financial_platform.dtos.responses.CheckAccountResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.*;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.TransactionErrorException;
import com.wallex.financial_platform.exceptions.transaction.TransactionNotFoundException;
import com.wallex.financial_platform.exceptions.card.CardNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.CardRepository;
import com.wallex.financial_platform.services.ITransactionService;
import com.wallex.financial_platform.services.utils.EncryptionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.repositories.TransactionRepository;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserContextService userContextService;
    private MovementService movementService;
    private EncryptionService encryptionService;
    private CardRepository cardRepository;
    private final NotificationService notificationService;

    @Override
    @SneakyThrows
    public TransactionResponseDTO getById(Long transactionId) {
        User user = this.userContextService.getAuthenticatedUser();
        Transaction foundTransaction = transactionRepository.findByTransactionIdAndUser(transactionId, user.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return mapToDTO(foundTransaction);
    }

    @Override
    @SneakyThrows
    public TransactionResponseDTO save(TransactionRequestDTO transactionReq) {
        User user = this.userContextService.getAuthenticatedUser();
        Account sourceAccount = user.getAccounts().stream().filter(acc -> Objects.equals(acc.getCbu(), transactionReq.sourceCbu()))
                .findAny().orElseThrow(()-> new AccountNotFoundException("Source Account not found"));
        Account destinationAccount = accountRepository.findByCbuOrAlias(transactionReq.destinationCbu(), transactionReq.destinationCbu())
                .orElseThrow(()-> new AccountNotFoundException("Destination Account not found"));
        if (sourceAccount.getCurrency() != destinationAccount.getCurrency()) {
            throw new TransactionErrorException("Currency mismatch");
        }
        if (sourceAccount.getAvailableBalance().compareTo(transactionReq.amount()) < 0) {
            throw new TransactionErrorException("Insufficient funds");
        }
        Transaction transaction = mapToEntity(transactionReq);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        if (transactionReq.type() == TransactionType.TRANSFER) {
            transaction.setStatus(TransactionStatus.COMPLETED);
        } else {
            transaction.setStatus(TransactionStatus.PENDING);
        }
        Transaction newTransaction = transactionRepository.save(transaction);
        movementService.save(newTransaction);

        // Notificar al usuario que envía la transferencia
        notificationService.notifyUser(
                sourceAccount.getUser(),
                "💸 Transferencia enviada",
                "📤 Has transferido " + transactionReq.amount() + " " + sourceAccount.getCurrency() +
                        " a la cuenta de " + destinationAccount.getUser().getFullName() + "."
        );

        // Notificar al usuario que recibe la transferencia
        notificationService.notifyUser(
                destinationAccount.getUser(),
                "💸 Transferencia recibida",
                "📥 Has recibido " + transactionReq.amount() + " " + destinationAccount.getCurrency() +
                        " de " + sourceAccount.getUser().getFullName() + "."
        );

        return mapToDTO(newTransaction);
    }

    public TransactionResponseDTO save(CardTransactionRequestDTO transactionReq) {
        User user = this.userContextService.getAuthenticatedUser();
        Card originCard = user.getCards().stream()
                .filter(card -> Objects.equals(encryptionService.decrypt(card.getEncryptedNumber()), transactionReq.cardNumber()))
                .findAny().orElseThrow(()-> new CardNotFoundException("Card not found"));
        Account destinationAccount =  accountRepository.findByCbuOrAlias(transactionReq.destinationCbu(), transactionReq.destinationCbu())
                .orElseThrow(()-> new AccountNotFoundException("Destination Account not found"));
        Account providerAccount = originCard.getProvider().getAccounts().stream().filter(acc -> acc.getCurrency() == destinationAccount.getCurrency())
                .findAny().orElseThrow(()-> new AccountNotFoundException("Card provider doesn't have any account with the same destination account currency"));
        Transaction cardTransaction = Transaction.builder()
                .sourceAccount(providerAccount)
                .destinationAccount(destinationAccount)
                .amount(transactionReq.amount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .reason(transactionReq.reason()+". Tarjeta "
                        +originCard.getProvider().getFullName()
                        +" **** **** **** "+transactionReq.cardNumber().substring(12))
                .build();
        originCard.getTransactions().add(cardTransaction);
        Card saved = cardRepository.save(originCard);
        Transaction savedT = saved.getTransactions().stream()
                .filter(transaction -> !originCard.getTransactions().contains(cardTransaction))
                .findAny().orElseThrow(()-> new TransactionErrorException("Transaction not found in card transaction list"));
//        movementService.save(Movement.builder()
//                .transaction(cardTransaction)
//                .user(user)
//                .build());
        return mapToDTO(savedT);
    }

    public TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                mapToDTO(transaction.getSourceAccount()),
                mapToDTO(transaction.getDestinationAccount()),
                transaction.getTransactionDateTime(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getReason()
        );
    }
    private CheckAccountResponseDTO mapToDTO(Account account) {
        return new CheckAccountResponseDTO(
                account.getCbu(),
                account.getAlias(),
                account.getCurrency(),
                account.getUser().getFullName(),
                account.getUser().getDni()
        );
    }

    private Transaction mapToEntity(TransactionRequestDTO transaction) {
        return Transaction.builder()
                .reason(transaction.reason())
                .amount(transaction.amount())
                .type(transaction.type())
                .transactionDateTime(LocalDateTime.now())
                .build();
    }
}

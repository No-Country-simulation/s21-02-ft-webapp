package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.MovementRequestDTO;
import com.wallex.financial_platform.dtos.responses.MovementResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Movement;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.TransactionNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.MovementRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.services.IMovementService;
import com.wallex.financial_platform.services.utils.AccountValidationUtils;
import com.wallex.financial_platform.services.utils.UserContextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovementService implements IMovementService {
    private static final String USERNAME_WALLEX = "Tesoreria Wallex";
    private static final String DESCRIPTION_RENDIMIENTO = "Se genera rendimiento por subida del dollar";

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserContextService userContextService;

    @Override
    @Transactional
    public MovementResponseDTO createMovement(MovementRequestDTO movementRequestDTO) {
        Account account = validateAccountExists(movementRequestDTO.accountId());
        Transaction transaction = validateTransactionIfExists(movementRequestDTO.transactionId());

        Movement movement = buildMovement(movementRequestDTO, account, transaction);
        movement = saveMovement(movement);

        return mapToDTO(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDTO> getMovementsByAccount(Long accountId) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        Account account = validateAccountExists(accountId);

        AccountValidationUtils.validateAccountOwnership(account, authenticatedUser);

        return mapMovementsToDTOs(findMovementsByAccountId(accountId));
    }

    // ========== Métodos privados reutilizables ==========

    private Account validateAccountExists(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
    }

    private Transaction validateTransactionIfExists(Long transactionId) {
        if (transactionId == null) {
            return null;
        }
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transacción no encontrada"));
    }


    private Movement buildMovement(MovementRequestDTO movementRequestDTO, Account account, Transaction transaction) {
        Movement movement = new Movement();
        movement.setAccount(account);
        movement.setTransaction(transaction);
        movement.setDescription(movementRequestDTO.description());
        movement.setAmount(movementRequestDTO.amount());
        movement.setMovementDate(movementRequestDTO.movementDate());
        return movement;
    }

        private Movement saveMovement(Movement movement) {
        return movementRepository.save(movement);
    }

    private MovementResponseDTO mapToDTO(Movement movement) {
        String userName = determineUserName(movement);
        String transactionType = determineTransactionType(movement, userName);

        return new MovementResponseDTO(
                movement.getMovementId(),
                movement.getAccount().getAccountId(),
                movement.getTransaction().getTransactionId(),
                movement.getDescription(),
                movement.getAmount(),
                movement.getMovementDate(),
                userName,
                transactionType
        );
    }

    private String determineUserName(Movement movement) {

        Transaction transaction = movement.getTransaction();
        if (transaction.getType() == TransactionType.TRANSFER) {
                return transaction.getDestinationAccount().getUser().getFullName();
        } else if (transaction.getType() == TransactionType.DEPOSIT) {
            return movement.getAccount().getUser().getFullName();
        }else if(transaction.getType() == TransactionType.RENDIMIENTO){
            return transaction.getSourceAccount().getUser().getFullName();
        }else if(transaction.getType() == TransactionType.RESERVE){
            return transaction.getSourceAccount().getUser().getFullName();
        }else if(transaction.getType() == TransactionType.RELEASE){
            return transaction.getSourceAccount().getUser().getFullName();
        }

        throw new IllegalStateException("Tipo de transacción no manejado: " + transaction.getType());
    }

    private String determineTransactionType(Movement movement, String userName) {
        return userName.equals(USERNAME_WALLEX)? TransactionType.RENDIMIENTO.name() : movement.getTransaction().getType().name();
    }

    private List<Movement> findMovementsByAccountId(Long accountId) {
        return movementRepository.findByAccount_AccountId(accountId);
    }

    private List<MovementResponseDTO> mapMovementsToDTOs(List<Movement> movements) {
        return movements.stream()
                .sorted(Comparator.comparing(Movement::getMovementDate).reversed())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}



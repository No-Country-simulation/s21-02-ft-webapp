package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.AccountResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.services.IAccountService;
import com.wallex.financial_platform.services.utils.EncryptionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final UserContextService userContextService;
    private final TransactionRepository transactionRepository;
    private final EncryptionService encryptionService;

    @Override
    public List<AccountResponseDTO> getAccountsByUser() {
        List<Account> accounts = this.accountRepository.findByUserId(this.userContextService.getAuthenticatedUser().getId());
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No se encontro cuentas del usuario");
        }
        return mapAccountsToDto(accounts);
    }

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountReq) {
        User authenticatedUser = this.userContextService.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Usuario autenticado no encontrado.");
        }

        validateExistingAccount(authenticatedUser, accountReq.currency());

        Account newAccount = buildNewAccount(authenticatedUser, accountReq.currency());

        this.accountRepository.save(newAccount);

        return convertToDTO(newAccount);
    }

    @Override
    public List<String> getCurrencies() {
        return Arrays.stream(CurrencyType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionResponseDTO transfer(TransferRequestDTO transferRequestDTO) {
        Long sourceAccountId = transferRequestDTO.sourceAccountId();
        String destinationIdentifier = transferRequestDTO.destinationIdentifier();
        BigDecimal amount = transferRequestDTO.amount();
        String reason = transferRequestDTO.reason();

        Account sourceAccount = this.accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de origen no encontrada"));

        Account destinationAccount = accountRepository.findByCbuOrAlias(destinationIdentifier, destinationIdentifier)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de destino no encontrada"));

        if (!sourceAccount.getUser().getId().equals(userContextService.getAuthenticatedUser().getId())) {
            throw new UserNotFoundException("No autorizado para operar esta cuenta");
        }

        if (sourceAccount.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes en la cuenta de origen");
        }

        // Realizar la transferencia
        sourceAccount.setAvailableBalance(sourceAccount.getAvailableBalance().subtract(amount));
        destinationAccount.setAvailableBalance(destinationAccount.getAvailableBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction(null, sourceAccount, destinationAccount, amount, TransactionType.TRANSFER, reason, null, TransactionStatus.COMPLETED);
        transaction = this.transactionRepository.save(transaction);

        // Devolver el DTO de la transacción completada
        return mapToDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO addFundsFromCard(DepositRequestDTO depositRequestDTO) {
        // Obtener al usuario autenticado
        User authenticatedUser = this.userContextService.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new UserNotFoundException("Usuario autenticado no encontrado.");
        }

        // Verificar que la cuenta existe y pertenece al usuario
        Account account = this.accountRepository.findById(depositRequestDTO.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada."));
        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccountErrorException("La cuenta no pertenece al usuario autenticado.");
        }

        // Buscar la tarjeta asociada al usuario
        Card card = authenticatedUser.getCards().stream()
                .filter(c -> this.encryptionService.decrypt(c.getEncryptedNumber()).equals(depositRequestDTO.cardNumber()))
                .findFirst()
                .orElseThrow(() -> new AccountErrorException("La tarjeta proporcionada no está asociada al usuario autenticado."));

        // Verificar que la tarjeta tiene saldo suficiente
        if (card.getBalance().compareTo(depositRequestDTO.amount()) < 0) {
            throw new InsufficientFundsException("La tarjeta no tiene saldo suficiente para realizar esta transacción.");
        }

        // Verificar que el monto a ingresar es válido
        if (depositRequestDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a ingresar debe ser mayor a cero.");
        }

        // Descontar el monto del saldo de la tarjeta
        card.setBalance(card.getBalance().subtract(depositRequestDTO.amount()));

        // Actualizar el saldo de la cuenta
        account.setAvailableBalance(account.getAvailableBalance().add(depositRequestDTO.amount()));
        this.accountRepository.save(account);

        // Registrar la transacción de ingreso
        Transaction transaction = new Transaction(
                null, // transactionId generado automáticamente
                account, // Cuenta de origen: no aplica para depósitos
                account, // Cuenta de destino
                depositRequestDTO.amount(),
                TransactionType.DEPOSIT, // Tipo de transacción: DEPÓSITO
                "Ingreso de fondos desde tarjeta " + depositRequestDTO.cardNumber(),
                null, // Fecha/hora asignada automáticamente
                TransactionStatus.COMPLETED // Estado completado
        );
        this.transactionRepository.save(transaction);

        // Retornar el DTO de la cuenta actualizada
        return mapToDTO(transaction);
    }


    @Override
    public List<AccountResponseDTO> getAccountsByUserAll() {
        List<Account> accounts = this.accountRepository.findAll();
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No se encontro cuentas del usuario");
        }
        return mapAccountsToDto(accounts);
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionDateTime(),
                transaction.getSourceAccount().getAccountId(),
                transaction.getDestinationAccount().getAccountId(),
                transaction.getAmount(),
                transaction.getReason(),
                transaction.getStatus().name()
        );
    }


    private void validateExistingAccount(User user, CurrencyType currency) {
        boolean accountExists = user.getAccounts().stream()
                .anyMatch(account -> account.getCurrency() == currency);

        if (accountExists) {
            throw new AccountErrorException("El usuario ya tiene una cuenta con esta moneda.");
        }
    }

    private Account buildNewAccount(User user, CurrencyType currency) {
        Faker faker = new Faker();

        Account account = new Account();
        account.setReservedBalance(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        account.setActive(true);
        account.setUser(user);
        account.setAlias(generateAlias(faker));
        account.setCbu(generateCbu(faker));
        account.setSourceTransactions(new ArrayList<>());
        account.setDestinationTransactions(new ArrayList<>());
        account.setReservations(new ArrayList<>());

        return account;
    }

    private String generateAlias(Faker faker) {
        return (faker.animal().name() + "." + faker.construction().materials() + "." + faker.commerce().material()).toLowerCase();
    }

    private String generateCbu(Faker faker) {
        return faker.numerify("CBU0000351Ø000000#######Ø");
    }

    private List<AccountResponseDTO> mapAccountsToDto(List<Account> accounts) {
        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountResponseDTO convertToDTO(Account account) {
        return new AccountResponseDTO(
                account.getAccountId(),
                account.getCbu(),
                account.getAlias(),
                account.getCurrency(),
                account.getAvailableBalance(),
                account.getReservedBalance()
        );
    }
}

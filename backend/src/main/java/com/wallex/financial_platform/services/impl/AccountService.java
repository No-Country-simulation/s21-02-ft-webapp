package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.*;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.account.CurrencyMismatchException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.services.IAccountService;
import com.wallex.financial_platform.services.utils.EncryptionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final UserContextService userContextService;
    private final TransactionService transactionService;
    private final EncryptionService encryptionService;
    private final NotificationService notificationService;
    private final ReservationService reservationService;
    private static long cbuCounter = 7;
    private final Random random = new Random();

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByUser() {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        List<Account> accounts = accountRepository.findByUserId(authenticatedUser.getId());
        validateAccountsNotEmpty(accounts, "No se encontraron cuentas para el usuario");
        return mapAccountsToDto(accounts);
    }

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountReq) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        validateExistingAccount(authenticatedUser, accountReq.currency());
        Account newAccount = buildNewAccount(authenticatedUser, accountReq.currency());
        accountRepository.save(newAccount);

        notificationService.notifyUser(
                authenticatedUser,
                "✨ Nueva cuenta creada con éxito",
                "🎉 ¡Has creado una nueva cuenta en " + accountReq.currency() + "! Ahora puedes realizar transacciones y gestionar tus finanzas.");

        return convertToDTO(newAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCurrencies() {
        return Arrays.stream(CurrencyType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionResponseDTO transfer(Long sourceAccountId, TransferRequestDTO transferRequestDTO) {
        Account sourceAccount = getAccountById(sourceAccountId);
        Account destinationAccount = getAccountByIdentifier(transferRequestDTO.destinationIdentifier());

        validateTransfer(sourceAccount, destinationAccount, transferRequestDTO.amount());

        performTransfer(sourceAccount, destinationAccount, transferRequestDTO.amount());
        return transactionService.createTransferTransaction(sourceAccount, destinationAccount, transferRequestDTO.amount(), transferRequestDTO.reason());
    }

    @Override
    @Transactional
    public TransactionResponseDTO addFundsFromCard(Long sourceAccountId, DepositRequestDTO depositRequestDTO) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        Account account = getAccountById(sourceAccountId);

        validateAccountOwnership(authenticatedUser, account);
        Card card = findUserCard(authenticatedUser, depositRequestDTO.cardNumber());
        validateCardBalance(card, depositRequestDTO.amount());

        performDeposit(account, card, depositRequestDTO.amount());
        return transactionService.createDepositTransaction(account, depositRequestDTO.amount(), depositRequestDTO.cardNumber());
    }

    @Override
    @Transactional
    public TransactionResponseDTO createReservation(Long sourceAccountId, ReservationRequestDTO reservationRequestDTO) {
        reservationService.createReservation(sourceAccountId, reservationRequestDTO);
        Account account = getAccountById(sourceAccountId);
        return transactionService.createReservationTransaction(account, reservationRequestDTO);
    }

    @Override
    @Transactional
    public TransactionResponseDTO releaseReservation(Long reservationId, Long accountId) {
        ReservationResponseDTO reservationResponseDTO = reservationService.releaseReservation(reservationId, accountId);
        Account account = getAccountById(accountId);
        return transactionService.releaseReservationTransaction(account, reservationResponseDTO);
    }

    @Override
    @Transactional
    public ValidateTransferResultResponseDTO validateAccountIdentifier(String identifier) {
        // Validación CBU (22 dígitos)
        if (identifier.length() == 22 && identifier.matches("[0-9]+")) {
            return accountRepository.findByCbuOrAlias(identifier, null)
                    .map(account -> new ValidateTransferResultResponseDTO(
                            true,
                            account.getUser().getFullName(),
                            "CBU"))
                    .orElse(new ValidateTransferResultResponseDTO(false, null, null));
        }

        // Validación Alias (6-20 caracteres alfanuméricos o puntos)
        if (identifier.length() >= 6 && identifier.length() <= 20 &&
                identifier.matches("[a-zA-Z0-9.]+")) {
            return accountRepository.findByCbuOrAlias(null, identifier.toLowerCase())
                    .map(account -> new ValidateTransferResultResponseDTO(
                            true,
                            account.getUser().getFullName(),
                            "Alias"))
                    .orElse(new ValidateTransferResultResponseDTO(false, null, null));
        }

        return new ValidateTransferResultResponseDTO(false, null, null);
    }

    @Override
    @Transactional
    public BalanceCheckResponseDTO checkAccountBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));

        boolean hasEnoughBalance = account.getAvailableBalance().compareTo(amount) >= 0;

        return new BalanceCheckResponseDTO(
                hasEnoughBalance,
                account.getAvailableBalance(),
                account.getCurrency().name()
        );
    }

    private void validateReservation(Account account, @NotNull @Positive BigDecimal amount) {
        validateAccountOwnership(account);
        validateAccountStatus(account);
        validateSufficientFunds(account, amount);
        validateCurrencyCompatibility(account, account);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByUserAll() {
        List<Account> accounts = accountRepository.findAll();
        validateAccountsNotEmpty(accounts, "No se encontró cuentas del usuario");
        return mapAccountsToDto(accounts);
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
    }

    private Account getAccountByIdentifier(String identifier) {
        return accountRepository.findByCbuOrAlias(identifier, identifier)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
    }

    private void validateTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        validateSameAccount(sourceAccount, destinationAccount);
        validateAccountOwnership(sourceAccount);
        validateAccountStatus(sourceAccount);
        validateAccountStatus(destinationAccount);
        validateSufficientFunds(sourceAccount, amount);
        validateCurrencyCompatibility(sourceAccount, destinationAccount);
    }

    private void validateSameAccount(Account sourceAccount, Account destinationAccount) {
        if (sourceAccount.getAccountId().equals(destinationAccount.getAccountId())) {
            throw new CurrencyMismatchException("No puedes transferir a la misma cuenta");
        }
    }

    private void validateAccountOwnership(Account account) {
        if (!account.getUser().getId().equals(userContextService.getAuthenticatedUser().getId())) {
            throw new CurrencyMismatchException("No autorizado para operar esta cuenta");
        }
    }

    private void validateAccountStatus(Account account) {
        if (!account.getActive()) {
            throw new AccountErrorException("La cuenta " + account.getAccountId() + " no está activa");
        }
    }

    private void validateCurrencyCompatibility(Account sourceAccount, Account destinationAccount) {
        if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            throw new AccountErrorException("Las cuentas deben tener la misma moneda para realizar la transferencia");
        }
    }

    private void validateSufficientFunds(Account sourceAccount, BigDecimal amount) {
        if (sourceAccount.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes en la cuenta de origen");
        }
    }

    private void performTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        sourceAccount.setAvailableBalance(sourceAccount.getAvailableBalance().subtract(amount));
        destinationAccount.setAvailableBalance(destinationAccount.getAvailableBalance().add(amount));
        accountRepository.saveAll(List.of(sourceAccount, destinationAccount));
    }

    private void validateAccountOwnership(User user, Account account) {
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccountErrorException("La cuenta no pertenece al usuario autenticado");
        }
    }

    private Card findUserCard(User user, String cardNumber) {
        return user.getCards().stream()
                .filter(c -> encryptionService.decrypt(c.getEncryptedNumber()).equals(cardNumber))
                .findFirst()
                .orElseThrow(() -> new AccountErrorException("La tarjeta proporcionada no está asociada al usuario autenticado"));
    }

    private void validateCardBalance(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("La tarjeta no tiene saldo suficiente para realizar esta transacción");
        }
    }

    private void performDeposit(Account account, Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().subtract(amount));
        account.setAvailableBalance(account.getAvailableBalance().add(amount));
        accountRepository.save(account);
    }

    private void validateExistingAccount(User user, CurrencyType currency) {
        boolean accountExists = user.getAccounts().stream()
                .anyMatch(account -> account.getCurrency() == currency);
        if (accountExists) {
            throw new AccountErrorException("El usuario ya tiene una cuenta con esta moneda");
        }
    }

    private Account buildNewAccount(User user, CurrencyType currency) {
        Account account = new Account();
        account.setReservedBalance(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        account.setActive(true);
        account.setUser(user);
        account.setAlias(generateAlias());
        account.setCbu(generateCbu());
        account.setSourceTransactions(new ArrayList<>());
        account.setDestinationTransactions(new ArrayList<>());
        account.setReservations(new ArrayList<>());
        return account;
    }

    private String generateAlias() {
        // Listas de palabras para generar alias aleatorios
        String[] animals = {"leon", "tigre", "aguila", "delfin", "lobo", "halcon", "pantera", "condor", "ballena", "jaguar"};
        String[] materials = {"oro", "plata", "bronce", "hierro", "acero", "cobre", "diamante", "rubi", "esmeralda", "zafiro"};
        String[] colors = {"rojo", "azul", "verde", "amarillo", "negro", "blanco", "violeta", "naranja", "rosado", "gris"};

        String animal = animals[random.nextInt(animals.length)];
        String material = materials[random.nextInt(materials.length)];
        String color = colors[random.nextInt(colors.length)];

        // Formato: animal.material.color
        return animal + "." + material + "." + color;
    }

    private String generateCbu() {
        // Genera un CBU único (22 dígitos)
        // Formato: 12312349 + 0000000000 + contador de 3 dígitos
        String base = "1231234900000000000"; // parte fija del CBU
        String sequence = String.format("%03d", cbuCounter); // rellena con ceros a la izquierda
        cbuCounter++;
        return base + sequence;
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

    private void validateAccountsNotEmpty(List<Account> accounts, String message) {
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(message);
        }
    }
}
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
        User authenticatedUser = userContextService.getAuthenticatedUser();
        List<Account> accounts = accountRepository.findByUserId(authenticatedUser.getId());
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No se encontraron cuentas para el usuario");
        }
        return mapAccountsToDto(accounts);
    }

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountReq) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        validateExistingAccount(authenticatedUser, accountReq.currency());
        Account newAccount = buildNewAccount(authenticatedUser, accountReq.currency());
        accountRepository.save(newAccount);
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
        Account sourceAccount = accountRepository.findById(transferRequestDTO.sourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de origen no encontrada"));
        Account destinationAccount = accountRepository.findByCbuOrAlias(transferRequestDTO.destinationIdentifier(), transferRequestDTO.destinationIdentifier())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de destino no encontrada"));

        validateTransferAuthorization(sourceAccount);
        validateSufficientFunds(sourceAccount, transferRequestDTO.amount());

        performTransfer(sourceAccount, destinationAccount, transferRequestDTO.amount());
        Transaction transaction = createTransaction(sourceAccount, destinationAccount, transferRequestDTO.amount(), transferRequestDTO.reason());
        transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO addFundsFromCard(DepositRequestDTO depositRequestDTO) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        Account account = accountRepository.findById(depositRequestDTO.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));

        validateAccountOwnership(authenticatedUser, account);
        Card card = findUserCard(authenticatedUser, depositRequestDTO.cardNumber());
        validateCardBalance(card, depositRequestDTO.amount());

        performDeposit(account, card, depositRequestDTO.amount());
        Transaction transaction = createDepositTransaction(account, depositRequestDTO.amount(), depositRequestDTO.cardNumber());
        transactionRepository.save(transaction);

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

    private void validateTransferAuthorization(Account sourceAccount) {
        if (!sourceAccount.getUser().getId().equals(userContextService.getAuthenticatedUser().getId())) {
            throw new UserNotFoundException("No autorizado para operar esta cuenta");
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
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    private Transaction createTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason) {
        return new Transaction(null, sourceAccount, destinationAccount, amount, TransactionType.TRANSFER, reason, null, TransactionStatus.COMPLETED);
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

    private Transaction createDepositTransaction(Account account, BigDecimal amount, String cardNumber) {
        return new Transaction(null, account, account, amount, TransactionType.DEPOSIT, "Ingreso de fondos desde tarjeta " + cardNumber, null, TransactionStatus.COMPLETED);
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
            throw new AccountErrorException("El usuario ya tiene una cuenta con esta moneda");
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





package com.wallex.financial_platform.services.utils;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.services.impl.AccountService;
import com.wallex.financial_platform.services.impl.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class DollarUpdateService {

    private final AccountRepository accountRepository;
    private final DollarService dollarService;
    private final TransactionService transactionService;
    private final AccountContextService accountContextService;

    private BigDecimal previousDollarValue;

    public DollarUpdateService(AccountRepository accountRepository, DollarService dollarService, TransactionService transactionService, AccountContextService accountContextService) {
        this.accountRepository = accountRepository;
        this.dollarService = dollarService;
        this.transactionService = transactionService;
        this.previousDollarValue = dollarService.getCurrentDollarValue();
        this.accountContextService = accountContextService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateBalancesBasedOnDollar() {
        BigDecimal currentDollarValue = dollarService.getCurrentDollarValue();
        if (currentDollarValue.compareTo(previousDollarValue) > 0) {
            BigDecimal dollarIncrease = currentDollarValue.subtract(previousDollarValue);
            List<Account> accounts = accountRepository.findByCurrency(CurrencyType.ARS);

            for (Account account : accounts) {
                if (account.getActive() && account.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0 && !account.getUser().getEmail().equals("tesoreria@wallex.com")) {
                    BigDecimal profit = account.getAvailableBalance()
                            .multiply(dollarIncrease.divide(previousDollarValue, 4, RoundingMode.HALF_UP));

                    account.setAvailableBalance(account.getAvailableBalance().add(profit));

                    this.transactionService.createYieldTransaction(accountContextService.getAccountWallexPesos(),account,profit, "Se genero rendimiento por subida del dollar" , TransactionType.RENDIMIENTO);
                    accountRepository.save(account);
                }
            }
        }

        previousDollarValue = currentDollarValue;
    }
}
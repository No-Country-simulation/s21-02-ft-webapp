package com.wallex.financial_platform.entities.listeners;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;

import com.wallex.financial_platform.entities.enums.TransactionType;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

public class AccountListener {

    @PostLoad
    @PostUpdate
    @PostPersist
    @PostRemove
    public void setBalances(Account account){
        Map<TransactionType, Map<TransactionStatus, BigDecimal>> balances = calculateBalance(account);
        account.setTransactionTypeBalances(balances);
        account.setUpdatedAt(LocalDateTime.now());
        account.setAvailableBalance(calculateAvailableBalance(balances));
    }


    private Map<TransactionType, Map<TransactionStatus, BigDecimal>> calculateBalance(Account account) {
        Map<TransactionType, Map<TransactionStatus, BigDecimal>> balances = generateMapping();

        if (!account.getDestinationTransactions().isEmpty()) {
            account.getDestinationTransactions().stream()
                    .forEach(transaction -> {
                        Map<TransactionStatus, BigDecimal> currentBalanceType = balances.get(transaction.getType());
                        currentBalanceType.put(
                                transaction.getStatus(),
                                currentBalanceType.get(transaction.getStatus()).add(transaction.getAmount())
                        );
                        balances.put(
                                transaction.getType(),
                                currentBalanceType
                        );
                    });
        }
        if (!account.getSourceTransactions().isEmpty()) {
            account.getSourceTransactions().stream()
                    .forEach(transaction -> {
                        Map<TransactionStatus, BigDecimal> currentBalanceType = balances.get(transaction.getType());
                        currentBalanceType.put(
                                transaction.getStatus(),
                                currentBalanceType.get(transaction.getStatus()).subtract(transaction.getAmount())
                        );
                        balances.put(
                                transaction.getType(),
                                currentBalanceType
                        );
                    });
        }
        return balances;
    }

    private Map<TransactionType, Map<TransactionStatus, BigDecimal>>  generateMapping() {
        Map<TransactionType, Map<TransactionStatus, BigDecimal>> mapping = new HashMap<>();
        for (TransactionType transactionType : TransactionType.values()) {
            Map<TransactionStatus, BigDecimal> balanceType = new HashMap<>();
            for (TransactionStatus transactionStatus : TransactionStatus.values()) {
                balanceType.put(transactionStatus, BigDecimal.ZERO);
            }
            mapping.put(transactionType, balanceType);
        }
        return mapping;
    }

    private BigDecimal calculateAvailableBalance(Map<TransactionType, Map<TransactionStatus, BigDecimal>> balances) {
        BigDecimal totalDeposits = balances.get(TransactionType.DEPOSIT).get(TransactionStatus.COMPLETED);
        BigDecimal totalWithdraws = balances.get(TransactionType.WITHDRAWAL).get(TransactionStatus.COMPLETED);
        BigDecimal totalTransfers = balances.get(TransactionType.TRANSFER).get(TransactionStatus.COMPLETED);
        BigDecimal totalInvestments = balances.get(TransactionType.INVESTMENT).get(TransactionStatus.COMPLETED);
        return totalDeposits.add(totalWithdraws).add(totalTransfers).add(totalInvestments);
    }
}

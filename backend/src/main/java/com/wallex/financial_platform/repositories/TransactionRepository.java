package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wallex.financial_platform.entities.Transaction;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value="SELECT * FROM transactions t " +
            "JOIN accounts acc ON acc.account_id = t.destination_account_account_id OR acc.account_id = t.source_account_account_id "+
            "JOIN users u ON acc.user_id = u.id " +
            "WHERE t.transaction_id = :transactionId AND u.id = :userId", nativeQuery = true)
    Optional<Transaction> findByTransactionIdAndUser(@Param("transactionId") Long transactionId, @Param("userId") Long userId);
}

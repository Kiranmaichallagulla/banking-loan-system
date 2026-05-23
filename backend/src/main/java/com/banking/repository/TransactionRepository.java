package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions for an account
    List<Transaction> findByAccount(Account account);

    // Find all transactions by account ID
    List<Transaction> findByAccountId(Long accountId);

    // Find transactions ordered by date (newest first)
    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);

    // Find transactions by account ID ordered by date
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}

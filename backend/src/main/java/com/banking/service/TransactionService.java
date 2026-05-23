package com.banking.service;

import com.banking.model.Transaction;
import com.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // ─── Get All Transactions for Account ────────────────
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    // ─── Get Transaction By ID ───────────────────────────
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Transaction not found with id: " + id));
    }

    // ─── Get All Transactions (Admin) ────────────────────
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    // ─── Create New Account ──────────────────────────────
    public Account createAccount(Long userId, Account.AccountType accountType) {
        User user = userService.getUserById(userId);

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);   // Start with zero balance
        account.setAccountType(accountType);
        account.setStatus(Account.AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }

    // ─── Get Account By ID ───────────────────────────────
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found!"));
    }

    // ─── Get All Accounts for a User ─────────────────────
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    // ─── Deposit Money ───────────────────────────────────
    public Account deposit(Long accountId, BigDecimal amount, String description) {

        // Business logic: amount must be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero!");
        }

        Account account = getAccountById(accountId);

        // Business logic: account must be active
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active!");
        }

        // Update balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Record transaction
        saveTransaction(account, Transaction.TransactionType.DEPOSIT,
                amount, description, account.getBalance());

        return account;
    }

    // ─── Withdraw Money ──────────────────────────────────
    public Account withdraw(Long accountId, BigDecimal amount, String description) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero!");
        }

        Account account = getAccountById(accountId);

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active!");
        }

        // Business logic: check sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        // Update balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Record transaction
        saveTransaction(account, Transaction.TransactionType.WITHDRAWAL,
                amount, description, account.getBalance());

        return account;
    }

    // ─── Transfer Money ──────────────────────────────────
    public void transfer(Long fromAccountId, Long toAccountId,
                         BigDecimal amount, String description) {

        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer to the same account!");
        }

        // Withdraw from sender
        withdraw(fromAccountId, amount, "Transfer to account: " + toAccountId);

        // Deposit to receiver
        deposit(toAccountId, amount, "Transfer from account: " + fromAccountId);
    }

    // ─── Get Transaction History ─────────────────────────
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    // ─── Private Helper: Save Transaction ────────────────
    private void saveTransaction(Account account,
                                 Transaction.TransactionType type,
                                 BigDecimal amount, String description,
                                 BigDecimal balanceAfter) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setBalanceAfter(balanceAfter);
        transactionRepository.save(transaction);
    }

    // ─── Private Helper: Generate Account Number ─────────
    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Generate random 10-digit number
            long number = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
            accountNumber = "ACC-" + number;
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}

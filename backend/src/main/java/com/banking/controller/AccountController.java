package com.banking.controller;

import com.banking.dto.TransactionRequest;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(
            @RequestParam Long userId,
            @RequestParam Account.AccountType type) {
        return ResponseEntity.ok(accountService.createAccount(userId, type));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getUserAccounts(
            @PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                accountService.deposit(accountId,
                        request.getAmount(), request.getDescription())
        );
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                accountService.withdraw(accountId,
                        request.getAmount(), request.getDescription())
        );
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<?> transfer(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        accountService.transfer(accountId,
                request.getToAccountId(),
                request.getAmount(),
                request.getDescription());
        return ResponseEntity.ok("Transfer successful!");
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                accountService.getTransactionHistory(accountId)
        );
    }
}
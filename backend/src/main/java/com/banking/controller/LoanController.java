package com.banking.controller;

import com.banking.dto.LoanRequest;
import com.banking.model.Loan;
import com.banking.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {

    private final LoanService loanService;

    // POST /api/loans/apply?userId=1
    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(
            @RequestParam Long userId,
            @Valid @RequestBody LoanRequest request) {
        return ResponseEntity.ok(
                loanService.applyForLoan(userId,
                        request.getAmount(),
                        request.getPurpose(),
                        request.getTermMonths())
        );
    }

    // GET /api/loans/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    // GET /api/loans/pending (Admin)
    @GetMapping("/pending")
    public ResponseEntity<List<Loan>> getPendingLoans() {
        return ResponseEntity.ok(loanService.getPendingLoans());
    }

    // GET /api/loans (Admin)
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    // PUT /api/loans/{loanId}/approve (Admin)
    @PutMapping("/{loanId}/approve")
    public ResponseEntity<Loan> approveLoan(
            @PathVariable Long loanId,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(loanService.approveLoan(loanId, remarks));
    }

    // PUT /api/loans/{loanId}/reject (Admin)
    @PutMapping("/{loanId}/reject")
    public ResponseEntity<Loan> rejectLoan(
            @PathVariable Long loanId,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(loanService.rejectLoan(loanId, remarks));
    }
}

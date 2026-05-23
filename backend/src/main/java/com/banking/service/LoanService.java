package com.banking.service;

import com.banking.model.Loan;
import com.banking.model.User;
import com.banking.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;

    // ─── Apply for Loan ──────────────────────────────────
    public Loan applyForLoan(Long userId, BigDecimal amount,
                             String purpose, Integer termMonths) {

        // Business logic: minimum loan amount
        if (amount.compareTo(new BigDecimal("1000")) < 0) {
            throw new RuntimeException("Minimum loan amount is $1000!");
        }

        // Business logic: maximum loan amount
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new RuntimeException("Maximum loan amount is $1,000,000!");
        }

        // Business logic: check if user already has a pending loan
        User user = userService.getUserById(userId);
        List<Loan> pendingLoans = loanRepository
                .findByUserAndStatus(user, Loan.LoanStatus.PENDING);

        if (!pendingLoans.isEmpty()) {
            throw new RuntimeException(
                    "You already have a pending loan application!");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAmount(amount);
        loan.setPurpose(purpose);
        loan.setTermMonths(termMonths);
        loan.setStatus(Loan.LoanStatus.PENDING);

        return loanRepository.save(loan);
    }

    // ─── Approve Loan (Admin only) ───────────────────────
    public Loan approveLoan(Long loanId, String adminRemarks) {
        Loan loan = getLoanById(loanId);

        // Business logic: can only approve PENDING loans
        if (loan.getStatus() != Loan.LoanStatus.PENDING) {
            throw new RuntimeException("Only pending loans can be approved!");
        }

        loan.setStatus(Loan.LoanStatus.APPROVED);
        loan.setAdminRemarks(adminRemarks);
        loan.setReviewedAt(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    // ─── Reject Loan (Admin only) ────────────────────────
    public Loan rejectLoan(Long loanId, String adminRemarks) {
        Loan loan = getLoanById(loanId);

        if (loan.getStatus() != Loan.LoanStatus.PENDING) {
            throw new RuntimeException("Only pending loans can be rejected!");
        }

        loan.setStatus(Loan.LoanStatus.REJECTED);
        loan.setAdminRemarks(adminRemarks);
        loan.setReviewedAt(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    // ─── Get Loan By ID ──────────────────────────────────
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Loan not found with id: " + id));
    }

    // ─── Get All Loans for a User ────────────────────────
    public List<Loan> getLoansByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return loanRepository.findByUser(user);
    }

    // ─── Get All Pending Loans (Admin) ───────────────────
    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatus(Loan.LoanStatus.PENDING);
    }

    // ─── Get All Loans (Admin) ───────────────────────────
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}

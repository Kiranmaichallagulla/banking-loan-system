package com.banking.repository;

import com.banking.model.Loan;
import com.banking.model.Loan.LoanStatus;
import com.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Find all loans for a specific user
    List<Loan> findByUser(User user);

    // Find all loans by user ID
    List<Loan> findByUserId(Long userId);

    // Find all loans by status (PENDING, APPROVED, REJECTED)
    List<Loan> findByStatus(LoanStatus status);

    // Find all loans for a user with a specific status
    List<Loan> findByUserAndStatus(User user, LoanStatus status);
}

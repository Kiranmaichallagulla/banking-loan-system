package com.banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                              // Many loans can belong to one user
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;             // Loan amount requested

    @Column(nullable = false)
    private String purpose;               // Why they need the loan

    @Column(nullable = false)
    private Integer termMonths;           // Loan duration in months

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;            // PENDING, APPROVED, REJECTED

    private String adminRemarks;          // Admin's note when approving/rejecting

    @CreationTimestamp
    private LocalDateTime appliedAt;

    private LocalDateTime reviewedAt;     // When admin reviewed it

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED
    }
}

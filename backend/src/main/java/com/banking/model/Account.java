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
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;           // e.g. "ACC-123456"

    @ManyToOne                             // Many accounts can belong to one user
    @JoinColumn(name = "user_id")          // Foreign key column in MySQL
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;            // Use BigDecimal for money (never float!)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;       // SAVINGS or CURRENT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;          // ACTIVE or INACTIVE

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AccountType {
        SAVINGS, CURRENT
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE
    }
}
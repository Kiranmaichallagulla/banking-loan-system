package com.banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data                          // Lombok: generates getters, setters, toString
@NoArgsConstructor             // Lombok: generates empty constructor
@AllArgsConstructor            // Lombok: generates constructor with all fields
@Entity                        // JPA: this class = a database table
@Table(name = "users")         // MySQL table name will be "users"
public class User {

    @Id                                                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // Auto increment
    private Long id;

    @Column(nullable = false)                   // Cannot be empty
    private String fullName;

    @Column(nullable = false, unique = true)    // Cannot be empty + must be unique
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)               // Store as "ADMIN" or "CUSTOMER"
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp                         // Auto set when user is created
    private LocalDateTime createdAt;

    // Enum inside the class — defines allowed roles
    public enum Role {
        ADMIN, CUSTOMER
    }
}
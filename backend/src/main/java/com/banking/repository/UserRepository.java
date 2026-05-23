package com.banking.repository;

import com.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository   // Marks this as a Spring Repository component
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query methods — Spring generates SQL automatically!

    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // SELECT * FROM users WHERE email = ? (returns true/false)
    boolean existsByEmail(String email);

    // SELECT * FROM users WHERE phone = ?
    Optional<User> findByPhone(String phone);
}

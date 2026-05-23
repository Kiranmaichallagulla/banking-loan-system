package com.banking.service;

import com.banking.model.User;
import com.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service                  // Marks this as a Spring Service component
@RequiredArgsConstructor  // Lombok: generates constructor for final fields
public class UserService {

    // @RequiredArgsConstructor generates constructor for these:
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── Register New User ───────────────────────────────
    public User registerUser(User user) {

        // Business logic: check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Business logic: encrypt password before saving
        // NEVER store plain text passwords!
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(User.Role.CUSTOMER);
        }

        return userRepository.save(user);
    }

    // ─── Get User By ID ──────────────────────────────────
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // ─── Get User By Email ───────────────────────────────
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // ─── Get All Users (Admin only) ──────────────────────
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ─── Update User ─────────────────────────────────────
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhone(updatedUser.getPhone());
        return userRepository.save(existingUser);
    }
}
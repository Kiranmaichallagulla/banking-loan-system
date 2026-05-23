package com.banking.controller;

import com.banking.config.JwtUtils;
import com.banking.dto.LoginRequest;
import com.banking.dto.RegisterRequest;
import com.banking.dto.AuthResponse;
import com.banking.model.User;
import com.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setRole(User.Role.CUSTOMER);

        User savedUser = userService.registerUser(user);
        String token = jwtUtils.generateToken(
                savedUser.getEmail(),
                savedUser.getRole().name()
        );

        return ResponseEntity.ok(
                new AuthResponse(token,
                        savedUser.getEmail(),
                        savedUser.getRole().name(),
                        savedUser.getFullName())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.getUserByEmail(request.getEmail());

            if (!passwordEncoder.matches(
                    request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body("Invalid password!");
            }

            String token = jwtUtils.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(
                    new AuthResponse(token,
                            user.getEmail(),
                            user.getRole().name(),
                            user.getFullName())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

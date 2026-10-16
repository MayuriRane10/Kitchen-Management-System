package com.kitchen.management.controller;

import com.kitchen.management.dto.ChangePasswordRequest;
import com.kitchen.management.dto.ForgotPasswordRequest;
import com.kitchen.management.dto.LoginRequest;
import com.kitchen.management.dto.LoginResponse;
import com.kitchen.management.dto.RegisterRequest;
import com.kitchen.management.dto.ResetPasswordRequest;
import com.kitchen.management.dto.UserResponse;
import com.kitchen.management.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================================================
    // 1. REGISTER ADMIN
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    // =========================================================
    // 2. ADMIN LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    // =========================================================
    // 3. ADMIN LOGOUT
    // =========================================================

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        return ResponseEntity.ok(
                "Logout successful. Please discard the JWT token."
        );
    }

    // =========================================================
    // 4. CHANGE PASSWORD
    // =========================================================

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        authService.changePassword(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }

    // =========================================================
    // 5. FORGOT PASSWORD
    // =========================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        String token =
                authService.forgotPassword(request);

        return ResponseEntity.ok(
                "Password reset token: " + token
        );
    }

    // =========================================================
    // 6. RESET PASSWORD
    // =========================================================

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}
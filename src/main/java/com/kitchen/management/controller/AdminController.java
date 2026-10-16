
package com.kitchen.management.controller;

import com.kitchen.management.entity.User;
import com.kitchen.management.entity.Role;
import com.kitchen.management.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET ADMIN PROFILE
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin can access profile");
        }

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }

    // UPDATE ADMIN EMAIL
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> request) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin can update profile");
        }

        String email = request.get("email");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email is required"));
        }

        String newEmail = email.trim();

        if (userRepository.existsByEmail(newEmail)
                && !user.getEmail().equalsIgnoreCase(newEmail)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email already registered"));
        }

        user.setEmail(newEmail);

        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Admin profile updated successfully",
                "email", updatedUser.getEmail(),
                "role", updatedUser.getRole().name()
        ));
    }
}

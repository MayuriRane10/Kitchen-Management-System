package com.kitchen.management.service;

import com.kitchen.management.dto.ChangePasswordRequest;
import com.kitchen.management.dto.ForgotPasswordRequest;
import com.kitchen.management.dto.LoginRequest;
import com.kitchen.management.dto.LoginResponse;
import com.kitchen.management.dto.RegisterRequest;
import com.kitchen.management.dto.ResetPasswordRequest;
import com.kitchen.management.dto.UserResponse;
import com.kitchen.management.entity.PasswordResetToken;
import com.kitchen.management.entity.Role;
import com.kitchen.management.entity.User;
import com.kitchen.management.repository.PasswordResetTokenRepository;
import com.kitchen.management.repository.UserRepository;
import com.kitchen.management.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;

        this.passwordResetTokenRepository =
                passwordResetTokenRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
    }

    // =========================================================
    // REGISTER ADMIN
    // =========================================================

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());

        user.setLastName(request.getLastName());

        user.setEmail(request.getEmail());

        user.setPhoneNo(request.getPhoneNo());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Admin role is assigned from backend
        user.setRole(Role.ADMIN);

        user.setActive(true);

        User savedUser =
                userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        // Check active account
        if (!Boolean.TRUE.equals(
                user.getActive())) {

            throw new RuntimeException(
                    "Account is inactive"
            );
        }

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // Check role
        if (!user.getRole().name()
                .equalsIgnoreCase(request.getRole())) {

            throw new RuntimeException(
                    "Invalid role"
            );
        }

        // =====================================================
        // GENERATE JWT
        // Email and role are stored inside JWT
        // =====================================================

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name()
                );

        // =====================================================
        // LOGIN RESPONSE
        // Role is NOT returned separately
        // =====================================================

        LoginResponse response =
                new LoginResponse();

        response.setToken(token);

        response.setUserId(
                user.getUserId()
        );

        response.setName(
                user.getFirstName()
                        + " "
                        + user.getLastName()
        );

        response.setEmail(
                user.getEmail()
        );

        return response;
    }
   
 // UPDATE ADMIN PROFILE
 @Transactional
 public UserResponse updateAdminProfile(
         String currentEmail,
         String firstName,
         String lastName,
         String email,
         String phoneNo) {

     User user = userRepository
             .findByEmail(currentEmail)
             .orElseThrow(() ->
                     new RuntimeException("Admin not found"));

     if (user.getRole() != Role.ADMIN) {
         throw new RuntimeException(
                 "Only admin can update profile");
     }

     if (!Boolean.TRUE.equals(user.getActive())) {
         throw new RuntimeException(
                 "Admin account is inactive");
     }

     if (firstName == null || firstName.isBlank()
             || lastName == null || lastName.isBlank()
             || email == null || email.isBlank()
             || phoneNo == null || phoneNo.isBlank()) {

         throw new RuntimeException(
                 "All profile fields are required");
     }

     String newEmail = email.trim();

     userRepository.findByEmail(newEmail)
             .ifPresent(existingUser -> {
                 if (!existingUser.getUserId()
                         .equals(user.getUserId())) {
                     throw new RuntimeException(
                             "Email already registered");
                 }
             });

     user.setFirstName(firstName.trim());
     user.setLastName(lastName.trim());
     user.setEmail(newEmail);
     user.setPhoneNo(phoneNo.trim());

     User updatedUser = userRepository.save(user);

     return convertToResponse(updatedUser);
 }
 

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    public void changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        // Check old password
        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Old password is incorrect"
            );
        }

        // Set new encrypted password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    @Transactional
    public String forgotPassword(
            ForgotPasswordRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Email not found"
                        )
                );

        // Delete previous reset token
        passwordResetTokenRepository
                .deleteByUser_UserId(
                        user.getUserId()
                );

        // Generate new reset token
        String token =
                UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                new PasswordResetToken();

        resetToken.setToken(token);

        resetToken.setUser(user);

        resetToken.setExpiryDate(
                LocalDateTime.now()
                        .plusMinutes(15)
        );

        passwordResetTokenRepository
                .save(resetToken);

        return token;
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    @Transactional
    public void resetPassword(
            ResetPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(
                                request.getToken()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid reset token"
                                )
                        );

        // Check expiry
        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Reset token has expired"
            );
        }

        User user =
                resetToken.getUser();

        // Set new encrypted password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        // Delete token after successful reset
        passwordResetTokenRepository
                .delete(resetToken);
    }

    // =========================================================
    // USER RESPONSE
    // Used for registration/profile
    // =========================================================

    public UserResponse convertToResponse(
            User user) {

        UserResponse response =
                new UserResponse();

        response.setUserId(
                user.getUserId()
        );

        response.setFirstName(
                user.getFirstName()
        );

        response.setLastName(
                user.getLastName()
        );

        response.setEmail(
                user.getEmail()
        );

        response.setPhoneNo(
                user.getPhoneNo()
        );

        response.setRole(
                user.getRole()
        );

        response.setActive(
                user.getActive()
        );

        response.setCreatedAt(
                user.getCreatedAt()
        );

        response.setUpdatedAt(
                user.getUpdatedAt()
        );

        return response;
    }
}
package com.kitchen.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    // Getter

    public String getEmail() {
        return email;
    }

    // Setter

    public void setEmail(String email) {
        this.email = email;
    }
}
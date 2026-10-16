package com.kitchen.management.dto;

public class DeliveryPersonLoginDTO {

    private String email;
    private String password;

    public DeliveryPersonLoginDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
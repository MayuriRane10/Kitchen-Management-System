package com.kitchen.management.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "deliveryPersonId",
        "name",
        "email",
        "role",
        "phoneNo",
        "isAvailable",
        "isActive",
        "isDeleted",
        "licence",
        "aadharCard",
        "insurance",
        "createdAt",
        "updatedAt",
        "cloudKitchen"
})
public class DeliveryPersonResponseDTO {

    private Long deliveryPersonId;

    private String name;

    private String email;

    private String phoneNo;

    private Boolean isAvailable;

    private Boolean isActive;

    private Boolean isDeleted;

    private String licence;

    private String aadharCard;

    private String insurance;

    private String role;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private CloudKitchenResponseDTO cloudKitchen;


    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(Long deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(String aadharCard) {
        this.aadharCard = aadharCard;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CloudKitchenResponseDTO getCloudKitchen() {
        return cloudKitchen;
    }

    public void setCloudKitchen(CloudKitchenResponseDTO cloudKitchen) {
        this.cloudKitchen = cloudKitchen;
    }
}
package com.kitchen.management.dto;

import org.springframework.web.multipart.MultipartFile;

public class DeliveryPersonRequestDTO {

    private String name;
    private String email;
    private String phoneNo;
    private String licence;
    private MultipartFile aadharCard;
    private String insurance;
    private String password;
    private Boolean isAvailable;
    private Boolean isActive;
    private Boolean isDeleted;
    private Long cloudKitchenId;

    public DeliveryPersonRequestDTO() {
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

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public MultipartFile getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(MultipartFile aadharCard) {
        this.aadharCard = aadharCard;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getCloudKitchenId() {
        return cloudKitchenId;
    }

    public void setCloudKitchenId(Long cloudKitchenId) {
        this.cloudKitchenId = cloudKitchenId;
    }
}
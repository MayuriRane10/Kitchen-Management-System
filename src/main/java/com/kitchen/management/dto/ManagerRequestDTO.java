package com.kitchen.management.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ManagerRequestDTO {

    private String name;
    private String email;
    private LocalDate dob;
    private String phoneNo;
    private String currentAddress;
    private String permanentAddress;

    private MultipartFile aadharCard;
    private MultipartFile panCard;
    private MultipartFile photo;

    private String password;
    private String city;

    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isFirstLogin;

    private Long cloudKitchenId;

    public ManagerRequestDTO() {
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public MultipartFile getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(MultipartFile aadharCard) {
        this.aadharCard = aadharCard;
    }

    public MultipartFile getPanCard() {
        return panCard;
    }

    public void setPanCard(MultipartFile panCard) {
        this.panCard = panCard;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Boolean getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Long getCloudKitchenId() {
        return cloudKitchenId;
    }

    public void setCloudKitchenId(Long cloudKitchenId) {
        this.cloudKitchenId = cloudKitchenId;
    }
}
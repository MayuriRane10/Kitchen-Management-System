package com.kitchen.management.dto;

public class CloudKitchenResponseDTO {

    private Long cloudKitchenId;
    private String state;
    private String city;
    private String division;
    private String address;
    private String pincode;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isOpened;

    public CloudKitchenResponseDTO() {
    }

    public CloudKitchenResponseDTO(Long cloudKitchenId, String state, String city,
                                   String division, String address, String pincode,
                                   Boolean isActive, Boolean isDeleted,
                                   Boolean isOpened) {
        this.cloudKitchenId = cloudKitchenId;
        this.state = state;
        this.city = city;
        this.division = division;
        this.address = address;
        this.pincode = pincode;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isOpened = isOpened;
    }

    public Long getCloudKitchenId() {
        return cloudKitchenId;
    }

    public void setCloudKitchenId(Long cloudKitchenId) {
        this.cloudKitchenId = cloudKitchenId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public Boolean getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        this.isOpened = isOpened;
    }
}
package com.kitchen.management.dto;

import java.time.LocalDateTime;

public class CuisineResponseDTO {

    private Long cuisineId;

    private String name;

    private String description;

    private String state;

    private String food;

    private Boolean isAvailable;

    private Boolean isUnavailable;

    private LocalDateTime createdAt;

    private LocalDateTime deleteAt;

    public CuisineResponseDTO() {
    }

    public Long getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Long cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getIsUnavailable() {
        return isUnavailable;
    }

    public void setIsUnavailable(Boolean isUnavailable) {
        this.isUnavailable = isUnavailable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
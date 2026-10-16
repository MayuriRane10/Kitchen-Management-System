package com.kitchen.management.dto;

import org.springframework.web.multipart.MultipartFile;

public class CuisineRequestDTO {

    private String name;

    private String description;

    private String state;

    private MultipartFile food;

    private Boolean isAvailable;

    private Boolean isUnavailable;

    public CuisineRequestDTO() {
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

    public MultipartFile getFood() {
        return food;
    }

    public void setFood(MultipartFile food) {
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
}
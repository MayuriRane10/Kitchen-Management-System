
package com.kitchen.management.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class MealRequestDTO {

    private String mealName;
    private String description;
    private String nutritionInformation;
    private MultipartFile mealPhoto;
    private BigDecimal price;
    private Boolean isAvailable = true;
    private Boolean isUnavailable = false;
    private Long cuisineId;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNutritionInformation() {
        return nutritionInformation;
    }

    public void setNutritionInformation(String nutritionInformation) {
        this.nutritionInformation = nutritionInformation;
    }

    public MultipartFile getMealPhoto() {
        return mealPhoto;
    }

    public void setMealPhoto(MultipartFile mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Long cuisineId) {
        this.cuisineId = cuisineId;
    }
}

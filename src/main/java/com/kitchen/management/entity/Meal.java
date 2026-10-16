
package com.kitchen.management.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @Column(nullable = false, length = 150)
    private String mealName;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String nutritionInformation;

    @Column(length = 500)
    private String mealPhoto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @Column(nullable = false)
    private boolean isUnavailable = false;

    // Many meals belong to one cuisine
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cuisine_id", nullable = false)
    private Cuisine cuisine;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (price == null) {
            price = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

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

    public String getMealPhoto() {
        return mealPhoto;
    }

    public void setMealPhoto(String mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("isAvailable")
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @JsonProperty("isUnavailable")
    public boolean getIsUnavailable() {
        return isUnavailable;
    }

    public void setIsUnavailable(boolean isUnavailable) {
        this.isUnavailable = isUnavailable;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }
}

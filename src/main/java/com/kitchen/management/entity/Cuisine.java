
package com.kitchen.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cuisine")
public class Cuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cuisineId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String food;

    @Column(nullable = false)
    private Boolean isAvailable;

    @Column(nullable = false)
    private Boolean isUnavailable;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deleteAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cloud_kitchen_id")
    @JsonIgnoreProperties({"reviews"})
    private CloudKitchen cloudKitchen;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (isAvailable == null) {
            isAvailable = true;
        }

        if (isUnavailable == null) {
            isUnavailable = false;
        }
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

    public CloudKitchen getCloudKitchen() {
        return cloudKitchen;
    }

    public void setCloudKitchen(CloudKitchen cloudKitchen) {
        this.cloudKitchen = cloudKitchen;
    }
}


package com.kitchen.management.service;

import com.kitchen.management.dto.MealRequestDTO;
import com.kitchen.management.entity.Cuisine;
import com.kitchen.management.entity.Meal;
import com.kitchen.management.repository.CuisineRepository;
import com.kitchen.management.repository.MealRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final CuisineRepository cuisineRepository;

    private final Path uploadDirectory =
            Paths.get("uploads", "meals").toAbsolutePath().normalize();

    public MealService(
            MealRepository mealRepository,
            CuisineRepository cuisineRepository) {
        this.mealRepository = mealRepository;
        this.cuisineRepository = cuisineRepository;
    }

    @Transactional
    public Meal saveMeal(MealRequestDTO dto) throws IOException {

        validateMeal(dto);

        Cuisine cuisine = cuisineRepository.findById(dto.getCuisineId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cuisine not found with ID: " + dto.getCuisineId()));

        Meal meal = new Meal();

        meal.setMealName(dto.getMealName().trim());
        meal.setDescription(dto.getDescription());
        meal.setNutritionInformation(dto.getNutritionInformation());
        meal.setPrice(dto.getPrice());
        meal.setIsAvailable(
                dto.getIsAvailable() == null || dto.getIsAvailable());
        meal.setIsUnavailable(
                dto.getIsUnavailable() != null && dto.getIsUnavailable());
        meal.setCuisine(cuisine);

        if (meal.getIsUnavailable()) {
            meal.setIsAvailable(false);
        }

        if (dto.getMealPhoto() != null && !dto.getMealPhoto().isEmpty()) {
            meal.setMealPhoto(savePhoto(dto.getMealPhoto()));
        }

        return mealRepository.save(meal);
    }

    @Transactional
    public Meal updateMeal(Long mealId, MealRequestDTO dto)
            throws IOException {

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Meal not found with ID: " + mealId));

        validateMeal(dto);

        Cuisine cuisine = cuisineRepository.findById(dto.getCuisineId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cuisine not found with ID: " + dto.getCuisineId()));

        meal.setMealName(dto.getMealName().trim());
        meal.setDescription(dto.getDescription());
        meal.setNutritionInformation(dto.getNutritionInformation());
        meal.setPrice(dto.getPrice());
        meal.setCuisine(cuisine);

        if (dto.getIsAvailable() != null) {
            meal.setIsAvailable(dto.getIsAvailable());
        }

        if (dto.getIsUnavailable() != null) {
            meal.setIsUnavailable(dto.getIsUnavailable());
        }

        if (meal.getIsUnavailable()) {
            meal.setIsAvailable(false);
        }

        if (dto.getMealPhoto() != null && !dto.getMealPhoto().isEmpty()) {
            String oldPhoto = meal.getMealPhoto();
            meal.setMealPhoto(savePhoto(dto.getMealPhoto()));
            deletePhoto(oldPhoto);
        }

        return mealRepository.save(meal);
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Transactional
    public void deleteMeal(Long mealId) {

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Meal not found with ID: " + mealId));

        String photo = meal.getMealPhoto();

        mealRepository.delete(meal);

        deletePhoto(photo);
    }

    private void validateMeal(MealRequestDTO dto) {

        if (dto.getMealName() == null ||
                dto.getMealName().trim().isEmpty()) {
            throw new IllegalArgumentException("Meal name is required");
        }

        if (dto.getCuisineId() == null) {
            throw new IllegalArgumentException("Cuisine ID is required");
        }

        if (dto.getPrice() == null ||
                dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price is required and cannot be negative");
        }
    }

    private String savePhoto(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null) {
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalName.substring(dotIndex).toLowerCase();
            }
        }

        if (!extension.matches("\\.(jpg|jpeg|png|webp)")) {
            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG, and WEBP images are allowed");
        }

        Files.createDirectories(uploadDirectory);

        String fileName = "meal_" + UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(fileName).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid image path");
        }

        file.transferTo(destination);

        return "/uploads/meals/" + fileName;
    }

    private void deletePhoto(String photoPath) {

        if (photoPath == null ||
                !photoPath.startsWith("/uploads/meals/")) {
            return;
        }

        try {
            String fileName = photoPath.substring(
                    "/uploads/meals/".length());

            if (fileName.contains("/") || fileName.contains("\\")) {
                return;
            }

            Path photo = uploadDirectory.resolve(fileName).normalize();

            if (photo.startsWith(uploadDirectory)) {
                Files.deleteIfExists(photo);
            }
        } catch (IOException exception) {
            // A photo cleanup failure should not hide a database operation.
            System.err.println(
                    "Could not delete meal photo: " + exception.getMessage());
        }
    }
}

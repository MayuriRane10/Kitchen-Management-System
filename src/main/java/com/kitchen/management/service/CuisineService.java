package com.kitchen.management.service;

import com.kitchen.management.dto.CuisineRequestDTO;
import com.kitchen.management.dto.CuisineResponseDTO;
import com.kitchen.management.entity.Cuisine;
import com.kitchen.management.repository.CuisineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CuisineService {

    @Autowired
    private CuisineRepository cuisineRepository;

    private final String uploadDirectory = "uploads/cuisine/";

    // =========================================================
    // SAVE CUISINE
    // =========================================================

    public CuisineResponseDTO saveCuisine(CuisineRequestDTO request) {

        Cuisine cuisine = new Cuisine();

        cuisine.setName(request.getName());
        cuisine.setDescription(request.getDescription());
        cuisine.setState(request.getState());

        if (request.getIsAvailable() == null) {
            cuisine.setIsAvailable(true);
        } else {
            cuisine.setIsAvailable(request.getIsAvailable());
        }

        if (request.getIsUnavailable() == null) {
            cuisine.setIsUnavailable(false);
        } else {
            cuisine.setIsUnavailable(request.getIsUnavailable());
        }

        MultipartFile food = request.getFood();

        if (food == null || food.isEmpty()) {
            throw new RuntimeException("Food file is required");
        }

        String foodPath = saveFoodFile(food);

        cuisine.setFood(foodPath);

        Cuisine savedCuisine = cuisineRepository.save(cuisine);

        return convertToResponse(savedCuisine);
    }

    // =========================================================
    // UPDATE CUISINE
    // =========================================================

    public CuisineResponseDTO updateCuisine(
            Long cuisineId,
            CuisineRequestDTO request) {

        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cuisine not found with ID: " + cuisineId
                        )
                );

        cuisine.setName(request.getName());
        cuisine.setDescription(request.getDescription());
        cuisine.setState(request.getState());

        if (request.getIsAvailable() != null) {
            cuisine.setIsAvailable(request.getIsAvailable());
        }

        if (request.getIsUnavailable() != null) {
            cuisine.setIsUnavailable(request.getIsUnavailable());
        }

        MultipartFile food = request.getFood();

        if (food != null && !food.isEmpty()) {

            String foodPath = saveFoodFile(food);

            cuisine.setFood(foodPath);
        }

        Cuisine updatedCuisine = cuisineRepository.save(cuisine);

        return convertToResponse(updatedCuisine);
    }

    // =========================================================
    // GET ALL CUISINE
    // =========================================================

    public List<CuisineResponseDTO> getAllCuisine() {

        return cuisineRepository.findAll()
                .stream()
                .filter(cuisine -> cuisine.getDeleteAt() == null)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET CUISINE BY ID
    // =========================================================

    public CuisineResponseDTO getCuisineById(Long cuisineId) {

        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cuisine not found with ID: " + cuisineId
                        )
                );

        if (cuisine.getDeleteAt() != null) {
            throw new RuntimeException(
                    "Cuisine has been deleted"
            );
        }

        return convertToResponse(cuisine);
    }

    // =========================================================
    // DELETE CUISINE
    // =========================================================

    public String deleteCuisineById(Long cuisineId) {

        Cuisine cuisine = cuisineRepository.findById(cuisineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cuisine not found with ID: " + cuisineId
                        )
                );

        cuisine.setDeleteAt(LocalDateTime.now());
        cuisine.setIsAvailable(false);
        cuisine.setIsUnavailable(true);

        cuisineRepository.save(cuisine);

        return "Cuisine deleted successfully";
    }

    // =========================================================
    // SAVE FOOD FILE
    // =========================================================

    private String saveFoodFile(MultipartFile file) {

        try {

            Path uploadPath =
                    Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName =
                    file.getOriginalFilename();

            if (originalFileName == null ||
                    originalFileName.isBlank()) {

                throw new RuntimeException(
                        "Invalid food file name"
                );
            }

            String extension = "";

            int lastDot =
                    originalFileName.lastIndexOf(".");

            if (lastDot >= 0) {
                extension =
                        originalFileName.substring(lastDot);
            }

            String fileName =
                    "food_"
                            + UUID.randomUUID()
                            + extension;

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/cuisine/" + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload food file",
                    e
            );
        }
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE
    // =========================================================

    private CuisineResponseDTO convertToResponse(
            Cuisine cuisine) {

        CuisineResponseDTO response =
                new CuisineResponseDTO();

        response.setCuisineId(
                cuisine.getCuisineId()
        );

        response.setName(
                cuisine.getName()
        );

        response.setDescription(
                cuisine.getDescription()
        );

        response.setState(
                cuisine.getState()
        );

        response.setFood(
                cuisine.getFood()
        );

        response.setIsAvailable(
                cuisine.getIsAvailable()
        );

        response.setIsUnavailable(
                cuisine.getIsUnavailable()
        );

        response.setCreatedAt(
                cuisine.getCreatedAt()
        );

        response.setDeleteAt(
                cuisine.getDeleteAt()
        );

        return response;
    }
}
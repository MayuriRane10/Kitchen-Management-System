package com.kitchen.management.controller;

import com.kitchen.management.dto.CuisineRequestDTO;
import com.kitchen.management.dto.CuisineResponseDTO;
import com.kitchen.management.service.CuisineService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuisine")
public class CuisineController {

    @Autowired
    private CuisineService cuisineService;

    // =========================================================
    // SAVE CUISINE
    // =========================================================

    @PostMapping("/saveCuisine")
    public ResponseEntity<CuisineResponseDTO> saveCuisine(
            @ModelAttribute CuisineRequestDTO request) {

        return ResponseEntity.ok(
                cuisineService.saveCuisine(request)
        );
    }

    // =========================================================
    // UPDATE CUISINE
    // =========================================================

    @PutMapping("/updateCuisine/{cuisineId}")
    public ResponseEntity<CuisineResponseDTO> updateCuisine(
            @PathVariable Long cuisineId,
            @ModelAttribute CuisineRequestDTO request) {

        return ResponseEntity.ok(
                cuisineService.updateCuisine(
                        cuisineId,
                        request
                )
        );
    }

    // =========================================================
    // GET ALL CUISINE
    // =========================================================

    @GetMapping("/getAllCuisine")
    public ResponseEntity<List<CuisineResponseDTO>> getAllCuisine() {

        return ResponseEntity.ok(
                cuisineService.getAllCuisine()
        );
    }

    // =========================================================
    // GET CUISINE BY ID
    // =========================================================

    @GetMapping("/getCuisineById/{cuisineId}")
    public ResponseEntity<CuisineResponseDTO> getCuisineById(
            @PathVariable Long cuisineId) {

        return ResponseEntity.ok(
                cuisineService.getCuisineById(cuisineId)
        );
    }

    // =========================================================
    // DELETE CUISINE
    // =========================================================

    @DeleteMapping("/deleteCuisineById/{cuisineId}")
    public ResponseEntity<String> deleteCuisineById(
            @PathVariable Long cuisineId) {

        return ResponseEntity.ok(
                cuisineService.deleteCuisineById(cuisineId)
        );
    }
}
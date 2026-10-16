
package com.kitchen.management.controller;

import com.kitchen.management.dto.MealRequestDTO;
import com.kitchen.management.entity.Meal;
import com.kitchen.management.service.MealService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    // SAVE MEAL
    @PostMapping(
        value = "/saveMeal",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Meal> saveMeal(
            @ModelAttribute MealRequestDTO dto) throws IOException {

        Meal savedMeal = mealService.saveMeal(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMeal);
    }

    // UPDATE MEAL
    @PutMapping(
        value = "/updateMeal/{mealId}",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Meal> updateMeal(
            @PathVariable Long mealId,
            @ModelAttribute MealRequestDTO dto) throws IOException {

        Meal updatedMeal = mealService.updateMeal(mealId, dto);

        return ResponseEntity.ok(updatedMeal);
    }

    // GET ALL MEALS
    @GetMapping("/getAllMeals")
    public ResponseEntity<List<Meal>> getAllMeals() {

        List<Meal> meals = mealService.getAllMeals();

        return ResponseEntity.ok(meals);
    }

    // DELETE MEAL
    @DeleteMapping("/deleteMeal/{mealId}")
    public ResponseEntity<Map<String, String>> deleteMeal(
            @PathVariable Long mealId) {

        mealService.deleteMeal(mealId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Meal deleted successfully");
        response.put("mealId", mealId.toString());

        return ResponseEntity.ok(response);
    }
}

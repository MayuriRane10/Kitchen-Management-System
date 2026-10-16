
package com.kitchen.management.controller;

import com.kitchen.management.dto.ChangePasswordRequest;
import com.kitchen.management.dto.ManagerLoginDTO;
import com.kitchen.management.dto.ManagerLoginResponseDTO;
import com.kitchen.management.dto.ManagerRequestDTO;
import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.entity.Manager;
import com.kitchen.management.entity.Cuisine;
import com.kitchen.management.entity.Meal;
import com.kitchen.management.service.ManagerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/login")
    public ResponseEntity<ManagerLoginResponseDTO> login(
            @Valid @RequestBody ManagerLoginDTO request) {
        return ResponseEntity.ok(managerService.login(request));
    }

    @PostMapping(value = "/saveManager",
            consumes = "multipart/form-data")
    public ResponseEntity<Manager> saveManager(
            @Valid @ModelAttribute ManagerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(managerService.saveManager(request));
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<List<Manager>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/getManagerById/{managerId}")
    public ResponseEntity<Manager> getManagerById(
            @PathVariable Long managerId) {
        return ResponseEntity.ok(
                managerService.getManagerById(managerId));
    }

    @GetMapping("/getAllCloudKitchens")
    public ResponseEntity<List<CloudKitchen>> getAllCloudKitchens() {
        return ResponseEntity.ok(
                managerService.getAllCloudKitchens());
    }

    @GetMapping("/getCloudKitchenById/{cloudKitchenId}")
    public ResponseEntity<CloudKitchen> getCloudKitchenById(
            @PathVariable Long cloudKitchenId) {
        return ResponseEntity.ok(
                managerService.getCloudKitchenById(cloudKitchenId));
    }

    @GetMapping("/getCloudKitchenStatus/{cloudKitchenId}")
    public ResponseEntity<Boolean> getCloudKitchenStatus(
            @PathVariable Long cloudKitchenId) {
        return ResponseEntity.ok(
                managerService.getCloudKitchenStatus(cloudKitchenId));
    }

    @GetMapping("/openCloseCloudKitchen/{cloudKitchenId}")
    public ResponseEntity<CloudKitchen> openCloseCloudKitchen(
            @PathVariable Long cloudKitchenId,
            @RequestParam Boolean isOpened) {
        return ResponseEntity.ok(
                managerService.openCloseCloudKitchen(
                        cloudKitchenId, isOpened));
    }

    @GetMapping("/getAllCuisines")
    public ResponseEntity<List<Cuisine>> getAllCuisines() {
        return ResponseEntity.ok(managerService.getAllCuisines());
    }

    @GetMapping("/getAllMeals")
    public ResponseEntity<List<Meal>> getAllMeals() {
        return ResponseEntity.ok(managerService.getAllMeals());
    }

    @GetMapping(
            "/getAvailableMealsByCloudKitchen/{cloudKitchenId}")
    public ResponseEntity<List<Meal>> getAvailableMealsByCloudKitchen(
            @PathVariable Long cloudKitchenId) {
        return ResponseEntity.ok(
                managerService.getAvailableMealsByCloudKitchen(
                        cloudKitchenId));
    }

    @PutMapping(value = "/updateManager/{managerId}",
            consumes = "multipart/form-data")
    public ResponseEntity<Manager> updateManager(
            @PathVariable Long managerId,
            @Valid @ModelAttribute ManagerRequestDTO request) {
        return ResponseEntity.ok(
                managerService.updateManager(managerId, request));
    }

    @PutMapping("/changePassword/{managerId}")
    public ResponseEntity<String> changePassword(
            @PathVariable Long managerId,
            @Valid @RequestBody ChangePasswordRequest request) {
        managerService.changePassword(managerId, request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/deleteManager/{managerId}")
    public ResponseEntity<String> deleteManager(
            @PathVariable Long managerId) {
        managerService.deleteManager(managerId);
        return ResponseEntity.ok("Manager deleted successfully");
    }
}

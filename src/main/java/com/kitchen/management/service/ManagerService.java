
package com.kitchen.management.service;

import com.kitchen.management.dto.ChangePasswordRequest;
import com.kitchen.management.dto.ManagerLoginDTO;
import com.kitchen.management.dto.ManagerLoginResponseDTO;
import com.kitchen.management.dto.ManagerRequestDTO;

import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.entity.Manager;
import com.kitchen.management.entity.Cuisine;
import com.kitchen.management.entity.Meal;

import com.kitchen.management.repository.CloudKitchenRepository;
import com.kitchen.management.repository.ManagerRepository;
import com.kitchen.management.repository.CuisineRepository;
import com.kitchen.management.repository.MealRepository;

import com.kitchen.management.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final CloudKitchenRepository cloudKitchenRepository;
    private final ManagerDocumentService managerDocumentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CuisineRepository cuisineRepository;
    private final MealRepository mealRepository;

    public ManagerService(
            ManagerRepository managerRepository,
            CloudKitchenRepository cloudKitchenRepository,
            ManagerDocumentService managerDocumentService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CuisineRepository cuisineRepository,
            MealRepository mealRepository) {

        this.managerRepository = managerRepository;
        this.cloudKitchenRepository = cloudKitchenRepository;
        this.managerDocumentService = managerDocumentService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cuisineRepository = cuisineRepository;
        this.mealRepository = mealRepository;
    }

    // VALIDATE CLOUD KITCHEN ASSIGNMENT
    private CloudKitchen validateKitchenAssignment(
            Long cloudKitchenId, Long currentManagerId) {

        if (cloudKitchenId == null) {
            throw new RuntimeException("Cloud Kitchen ID is required");
        }

        CloudKitchen kitchen = cloudKitchenRepository
                .findById(cloudKitchenId)
                .orElseThrow(() -> new RuntimeException(
                        "Cloud Kitchen not found with ID: "
                                + cloudKitchenId));

        if (!Boolean.TRUE.equals(kitchen.getIsActive())) {
            throw new RuntimeException(
                    "Cannot assign an inactive cloud kitchen");
        }

        if (Boolean.TRUE.equals(kitchen.getIsDeleted())) {
            throw new RuntimeException(
                    "Cannot assign a deleted cloud kitchen");
        }

        managerRepository
                .findByCloudKitchen_CloudKitchenId(cloudKitchenId)
                .ifPresent(existingManager -> {
                    boolean assignedToAnotherManager =
                            !Objects.equals(
                                    existingManager.getManagerId(),
                                    currentManagerId);

                    if (assignedToAnotherManager) {
                        throw new RuntimeException(
                                "This cloud kitchen is already assigned "
                                        + "to another manager");
                    }
                });

        return kitchen;
    }

    // SAVE MANAGER
    @Transactional
    public Manager saveManager(ManagerRequestDTO request) {

        if (managerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        CloudKitchen kitchen = validateKitchenAssignment(
                request.getCloudKitchenId(), null);

        Manager manager = new Manager();

        manager.setName(request.getName());
        manager.setEmail(request.getEmail());
        manager.setDob(request.getDob());
        manager.setPhoneNo(request.getPhoneNo());
        manager.setCurrentAddress(request.getCurrentAddress());
        manager.setPermanentAddress(request.getPermanentAddress());
        manager.setPassword(
                passwordEncoder.encode(request.getPassword()));
        manager.setCity(request.getCity());

        manager.setCloudKitchen(kitchen);
        manager.setIsActive(true);
        manager.setIsDeleted(false);
        manager.setIsFirstLogin(true);

        return managerRepository.save(manager);
    }

    // GET ALL MANAGERS
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    // GET MANAGER BY ID
    public Manager getManagerById(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found with ID: " + managerId));
    }

    // GET ALL CLOUD KITCHENS
    public List<CloudKitchen> getAllCloudKitchens() {
        return cloudKitchenRepository.findAll();
    }

    // GET CLOUD KITCHEN BY ID
    public CloudKitchen getCloudKitchenById(Long cloudKitchenId) {
        return cloudKitchenRepository.findById(cloudKitchenId)
                .orElseThrow(() -> new RuntimeException(
                        "Cloud Kitchen not found with ID: "
                                + cloudKitchenId));
    }

    // OPEN OR CLOSE CLOUD KITCHEN
    public CloudKitchen openCloseCloudKitchen(
            Long cloudKitchenId, Boolean isOpened) {

        if (isOpened == null) {
            throw new RuntimeException(
                    "isOpened must be true or false");
        }

        CloudKitchen kitchen = getCloudKitchenById(cloudKitchenId);
        kitchen.setIsOpened(isOpened);

        return cloudKitchenRepository.save(kitchen);
    }

    // GET CLOUD KITCHEN OPEN/CLOSED STATUS
    public Boolean getCloudKitchenStatus(Long cloudKitchenId) {
        CloudKitchen kitchen = getCloudKitchenById(cloudKitchenId);
        return kitchen.getIsOpened();
    }

    // GET ALL CUISINES
    public List<Cuisine> getAllCuisines() {
        return cuisineRepository.findAll();
    }

    // GET ALL MEALS
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    // GET AVAILABLE MEALS FOR A SPECIFIC CLOUD KITCHEN
    @Transactional(readOnly = true)
    public List<Meal> getAvailableMealsByCloudKitchen(
            Long cloudKitchenId) {

        if (!cloudKitchenRepository.existsById(cloudKitchenId)) {
            throw new RuntimeException(
                    "Cloud Kitchen not found with ID: " + cloudKitchenId);
        }

        return mealRepository.findAvailableMealsByCloudKitchenId(
                cloudKitchenId);
    }

    // UPDATE MANAGER
    @Transactional
    public Manager updateManager(
            Long managerId, ManagerRequestDTO request) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found with ID: " + managerId));

        if (request.getEmail() != null
                && !request.getEmail().equals(manager.getEmail())
                && managerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getName() != null) {
            manager.setName(request.getName());
        }
        if (request.getEmail() != null) {
            manager.setEmail(request.getEmail());
        }
        if (request.getDob() != null) {
            manager.setDob(request.getDob());
        }
        if (request.getPhoneNo() != null) {
            manager.setPhoneNo(request.getPhoneNo());
        }
        if (request.getCurrentAddress() != null) {
            manager.setCurrentAddress(request.getCurrentAddress());
        }
        if (request.getPermanentAddress() != null) {
            manager.setPermanentAddress(request.getPermanentAddress());
        }
        if (request.getCity() != null) {
            manager.setCity(request.getCity());
        }

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {
            manager.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        if (request.getCloudKitchenId() != null) {
            CloudKitchen kitchen = validateKitchenAssignment(
                    request.getCloudKitchenId(), managerId);
            manager.setCloudKitchen(kitchen);
        }

        return managerRepository.save(manager);
    }

    // MANAGER LOGIN
    public ManagerLoginResponseDTO login(ManagerLoginDTO request) {

        Manager manager = managerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "Invalid email or password"));

        if (Boolean.TRUE.equals(manager.getIsDeleted())) {
            throw new RuntimeException("Manager account is deleted");
        }

        if (!Boolean.TRUE.equals(manager.getIsActive())) {
            throw new RuntimeException("Manager account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(), manager.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!"MANAGER".equalsIgnoreCase(request.getRole())) {
            throw new RuntimeException("Invalid role");
        }

        String token = jwtService.generateToken(
                manager.getEmail(), "MANAGER");

        ManagerLoginResponseDTO response =
                new ManagerLoginResponseDTO();

        response.setToken(token);
        response.setManagerId(manager.getManagerId());
        response.setName(manager.getName());
        response.setEmail(manager.getEmail());

        return response;
    }

    // GET ONLY THE LOGGED-IN MANAGER'S ASSIGNED CLOUD KITCHEN
    @Transactional(readOnly = true)
    public CloudKitchen getCloudKitchenForLoggedInManager(String email) {

        Manager manager = managerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found for logged-in email"));

        if (Boolean.TRUE.equals(manager.getIsDeleted())
                || !Boolean.TRUE.equals(manager.getIsActive())) {
            throw new RuntimeException(
                    "Manager account is inactive or deleted");
        }

        CloudKitchen kitchen = manager.getCloudKitchen();

        if (kitchen == null) {
            throw new RuntimeException(
                    "No cloud kitchen is assigned to this manager");
        }

        return kitchen;
    }

    // CHANGE PASSWORD
    public void changePassword(
            Long managerId, ChangePasswordRequest request) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found with ID: " + managerId));

        if (!passwordEncoder.matches(
                request.getOldPassword(), manager.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        manager.setPassword(
                passwordEncoder.encode(request.getNewPassword()));
        manager.setIsFirstLogin(false);

        managerRepository.save(manager);
    }

    // DELETE MANAGER (SOFT DELETE)
    public void deleteManager(Long managerId) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException(
                        "Manager not found with ID: " + managerId));

        manager.setIsDeleted(true);
        manager.setIsActive(false);

        managerRepository.save(manager);
    }
}

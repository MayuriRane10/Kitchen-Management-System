package com.kitchen.management.service;

import com.kitchen.management.dto.CloudKitchenResponseDTO;
import com.kitchen.management.dto.DeliveryPersonChangePasswordDTO;
import com.kitchen.management.dto.DeliveryPersonLoginDTO;
import com.kitchen.management.dto.DeliveryPersonRequestDTO;
import com.kitchen.management.dto.DeliveryPersonResponseDTO;
import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.entity.DeliveryPerson;
import com.kitchen.management.repository.CloudKitchenRepository;
import com.kitchen.management.repository.DeliveryPersonRepository;
import com.kitchen.management.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final CloudKitchenRepository cloudKitchenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public DeliveryPersonService(
            DeliveryPersonRepository deliveryPersonRepository,
            CloudKitchenRepository cloudKitchenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.deliveryPersonRepository = deliveryPersonRepository;
        this.cloudKitchenRepository = cloudKitchenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // SAVE DELIVERY PERSON
    public DeliveryPersonResponseDTO saveDeliveryPerson(
            DeliveryPersonRequestDTO request) {

        if (deliveryPersonRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException("Email already exists");
        }

        if (request.getLicence() != null
                && !request.getLicence().matches("^[A-Za-z0-9]+$")) {

            throw new RuntimeException(
                    "Licence must contain only letters and numbers");
        }

        DeliveryPerson deliveryPerson = new DeliveryPerson();

        deliveryPerson.setName(request.getName());
        deliveryPerson.setEmail(request.getEmail());
        deliveryPerson.setPhoneNo(request.getPhoneNo());
        deliveryPerson.setLicence(request.getLicence());
        deliveryPerson.setInsurance(request.getInsurance());

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException("Password is required");
        }

        deliveryPerson.setPassword(
                passwordEncoder.encode(request.getPassword()));

        deliveryPerson.setIsAvailable(true);
        deliveryPerson.setIsActive(true);
        deliveryPerson.setIsDeleted(false);
        deliveryPerson.setRole("DELIVERY_PERSON");

        deliveryPerson.setCreatedAt(LocalDateTime.now());
        deliveryPerson.setUpdatedAt(LocalDateTime.now());

        if (request.getCloudKitchenId() != null) {

            CloudKitchen cloudKitchen =
                    cloudKitchenRepository
                            .findById(request.getCloudKitchenId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cloud Kitchen not found with ID: "
                                                    + request.getCloudKitchenId()));

            deliveryPerson.setCloudKitchen(cloudKitchen);
        }

        DeliveryPerson saved =
                deliveryPersonRepository.save(deliveryPerson);

        if (request.getAadharCard() != null
                && !request.getAadharCard().isEmpty()) {

            String aadharUrl =
                    saveAadharFile(
                            request.getAadharCard(),
                            saved.getDeliveryPersonId());

            saved.setAadharCard(aadharUrl);
            saved.setUpdatedAt(LocalDateTime.now());

            saved = deliveryPersonRepository.save(saved);
        }

        return convertToResponse(saved);
    }

    // GET ALL DELIVERY PERSONS
    public List<DeliveryPersonResponseDTO> getAllDeliveryPersons() {

        return deliveryPersonRepository
                .findAll()
                .stream()
                .filter(dp ->
                        !Boolean.TRUE.equals(dp.getIsDeleted()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // GET DELIVERY PERSON BY ID
    public DeliveryPersonResponseDTO getDeliveryPersonById(
            Long deliveryPersonId) {

        DeliveryPerson deliveryPerson =
                deliveryPersonRepository
                        .findById(deliveryPersonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Delivery Person not found with ID: "
                                                + deliveryPersonId));

        if (Boolean.TRUE.equals(
                deliveryPerson.getIsDeleted())) {

            throw new RuntimeException(
                    "Delivery Person is deleted");
        }

        return convertToResponse(deliveryPerson);
    }

    // UPDATE DELIVERY PERSON
    public DeliveryPersonResponseDTO updateDeliveryPerson(
            Long deliveryPersonId,
            DeliveryPersonRequestDTO request) {

        DeliveryPerson deliveryPerson =
                deliveryPersonRepository
                        .findById(deliveryPersonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Delivery Person not found with ID: "
                                                + deliveryPersonId));

        if (Boolean.TRUE.equals(
                deliveryPerson.getIsDeleted())) {

            throw new RuntimeException(
                    "Delivery Person is deleted");
        }

        if (request.getName() != null) {
            deliveryPerson.setName(request.getName());
        }

        if (request.getPhoneNo() != null) {
            deliveryPerson.setPhoneNo(request.getPhoneNo());
        }

        if (request.getLicence() != null) {

            if (!request.getLicence()
                    .matches("^[A-Za-z0-9]+$")) {

                throw new RuntimeException(
                        "Licence must contain only letters and numbers");
            }

            deliveryPerson.setLicence(request.getLicence());
        }

        if (request.getInsurance() != null) {
            deliveryPerson.setInsurance(request.getInsurance());
        }

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            deliveryPerson.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()));
        }

        if (request.getIsAvailable() != null) {
            deliveryPerson.setIsAvailable(
                    request.getIsAvailable());
        }

        if (request.getIsActive() != null) {
            deliveryPerson.setIsActive(
                    request.getIsActive());
        }

        if (request.getCloudKitchenId() != null) {

            CloudKitchen cloudKitchen =
                    cloudKitchenRepository
                            .findById(request.getCloudKitchenId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cloud Kitchen not found with ID: "
                                                    + request.getCloudKitchenId()));

            deliveryPerson.setCloudKitchen(cloudKitchen);
        }

        if (request.getAadharCard() != null
                && !request.getAadharCard().isEmpty()) {

            String aadharUrl =
                    saveAadharFile(
                            request.getAadharCard(),
                            deliveryPerson.getDeliveryPersonId());

            deliveryPerson.setAadharCard(aadharUrl);
        }

        deliveryPerson.setUpdatedAt(LocalDateTime.now());

        DeliveryPerson updated =
                deliveryPersonRepository.save(deliveryPerson);

        return convertToResponse(updated);
    }

    // DELETE DELIVERY PERSON
    public String deleteDeliveryPerson(Long deliveryPersonId) {

        DeliveryPerson deliveryPerson =
                deliveryPersonRepository
                        .findById(deliveryPersonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Delivery Person not found with ID: "
                                                + deliveryPersonId));

        deliveryPerson.setIsDeleted(true);
        deliveryPerson.setIsActive(false);
        deliveryPerson.setIsAvailable(false);
        deliveryPerson.setUpdatedAt(LocalDateTime.now());

        deliveryPersonRepository.save(deliveryPerson);

        return "Delivery Person deleted successfully";
    }

    // DELIVERY PERSON LOGIN
    public DeliveryPersonResponseDTO login(
            DeliveryPersonLoginDTO request) {

        DeliveryPerson deliveryPerson =
                deliveryPersonRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid email or password"));

        if (!Boolean.TRUE.equals(
                deliveryPerson.getIsActive())) {

            throw new RuntimeException(
                    "Delivery Person account is inactive");
        }

        if (Boolean.TRUE.equals(
                deliveryPerson.getIsDeleted())) {

            throw new RuntimeException(
                    "Delivery Person account is deleted");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                deliveryPerson.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        String role = deliveryPerson.getRole();

        if (role == null || role.isBlank()) {
            role = "DELIVERY_PERSON";
        }

        String token =
                jwtService.generateToken(
                        deliveryPerson.getEmail(),
                        role);

        DeliveryPersonResponseDTO response =
                new DeliveryPersonResponseDTO();

        response.setToken(token);

        response.setDeliveryPersonId(
                deliveryPerson.getDeliveryPersonId());

        response.setName(
                deliveryPerson.getName());

        response.setEmail(
                deliveryPerson.getEmail());

        response.setRole(role);

        return response;
    }

    // CHANGE PASSWORD
    public String changePassword(
            Long deliveryPersonId,
            DeliveryPersonChangePasswordDTO request) {

        DeliveryPerson deliveryPerson =
                deliveryPersonRepository
                        .findById(deliveryPersonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Delivery Person not found with ID: "
                                                + deliveryPersonId));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                deliveryPerson.getPassword())) {

            throw new RuntimeException(
                    "Current password is incorrect");
        }

        if (request.getNewPassword() == null
                || request.getNewPassword().isBlank()) {

            throw new RuntimeException(
                    "New password is required");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new RuntimeException(
                    "New password and confirm password do not match");
        }

        deliveryPerson.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        deliveryPerson.setUpdatedAt(LocalDateTime.now());

        deliveryPersonRepository.save(deliveryPerson);

        return "Password changed successfully";
    }

    // SAVE AADHAAR FILE
    private String saveAadharFile(
            MultipartFile file,
            Long deliveryPersonId) {

        try {

            String uploadDir =
                    System.getProperty("user.dir")
                            + "/uploads/delivery-persons";

            Path uploadPath =
                    Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf("."));
            }

            String fileName =
                    deliveryPersonId
                            + "_aadhar"
                            + extension;

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:8080/uploads/delivery-persons/"
                    + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload Aadhaar file",
                    e);
        }
    }

    // CONVERT TO RESPONSE
    private DeliveryPersonResponseDTO convertToResponse(
            DeliveryPerson deliveryPerson) {

        DeliveryPersonResponseDTO response =
                new DeliveryPersonResponseDTO();

        response.setDeliveryPersonId(
                deliveryPerson.getDeliveryPersonId());

        response.setName(
                deliveryPerson.getName());

        response.setEmail(
                deliveryPerson.getEmail());

        response.setPhoneNo(
                deliveryPerson.getPhoneNo());

        response.setIsAvailable(
                deliveryPerson.getIsAvailable());

        response.setIsActive(
                deliveryPerson.getIsActive());

        response.setIsDeleted(
                deliveryPerson.getIsDeleted());

        response.setLicence(
                deliveryPerson.getLicence());

        response.setAadharCard(
                deliveryPerson.getAadharCard());

        response.setInsurance(
                deliveryPerson.getInsurance());

        response.setRole(
                deliveryPerson.getRole());

        response.setCreatedAt(
                deliveryPerson.getCreatedAt());

        response.setUpdatedAt(
                deliveryPerson.getUpdatedAt());

        if (deliveryPerson.getCloudKitchen() != null) {

            CloudKitchen cloudKitchen =
                    deliveryPerson.getCloudKitchen();

            CloudKitchenResponseDTO kitchenResponse =
                    new CloudKitchenResponseDTO();

            kitchenResponse.setCloudKitchenId(
                    cloudKitchen.getCloudKitchenId());

            kitchenResponse.setState(
                    cloudKitchen.getState());

            kitchenResponse.setCity(
                    cloudKitchen.getCity());

            kitchenResponse.setDivision(
                    cloudKitchen.getDivision());

            kitchenResponse.setAddress(
                    cloudKitchen.getAddress());

            kitchenResponse.setPincode(
                    cloudKitchen.getPincode());

            kitchenResponse.setIsActive(
                    cloudKitchen.getIsActive());

            kitchenResponse.setIsDeleted(
                    cloudKitchen.getIsDeleted());

            kitchenResponse.setIsOpened(
                    cloudKitchen.getIsOpened());

            response.setCloudKitchen(
                    kitchenResponse);
        }

        return response;
    }
}
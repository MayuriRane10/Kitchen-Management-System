package com.kitchen.management.service;

import com.kitchen.management.entity.Manager;
import com.kitchen.management.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ManagerDocumentService {

    private final String uploadDirectory = "uploads/managers/";

    @Autowired
    private ManagerRepository managerRepository;

    // =========================================================
    // UPLOAD ALL MANAGER DOCUMENTS
    // =========================================================

    public Map<String, String> uploadDocuments(
            Long managerId,
            MultipartFile aadharCard,
            MultipartFile panCard,
            MultipartFile photo) {

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Manager not found with ID: " + managerId
                        )
                );

        Map<String, String> response = new HashMap<>();

        if (aadharCard != null && !aadharCard.isEmpty()) {

            String aadharPath =
                    saveDocument(aadharCard, "aadhar");

            manager.setAadharCard(aadharPath);
            response.put("aadharCard", aadharPath);
        }

        if (panCard != null && !panCard.isEmpty()) {

            String panPath =
                    saveDocument(panCard, "pan");

            manager.setPanCard(panPath);
            response.put("panCard", panPath);
        }

        if (photo != null && !photo.isEmpty()) {

            String photoPath =
                    saveDocument(photo, "photo");

            manager.setPhoto(photoPath);
            response.put("photo", photoPath);
        }

        managerRepository.save(manager);

        response.put(
                "message",
                "Manager documents uploaded successfully"
        );

        return response;
    }

    // =========================================================
    // SAVE SINGLE DOCUMENT
    // =========================================================

    public String saveDocument(
            MultipartFile file,
            String documentType) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Create upload directory
            Path uploadPath =
                    Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Original filename
            String originalFileName =
                    file.getOriginalFilename();

            if (originalFileName == null ||
                    originalFileName.isBlank()) {

                throw new RuntimeException(
                        "Invalid file name"
                );
            }

            // Get extension
            String extension = "";

            int lastDot =
                    originalFileName.lastIndexOf(".");

            if (lastDot >= 0) {
                extension =
                        originalFileName.substring(lastDot);
            }

            // Generate unique filename
            String fileName =
                    documentType
                            + "_"
                            + UUID.randomUUID()
                            + extension;

            // Complete file path
            Path filePath =
                    uploadPath.resolve(fileName);

            // Save file
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Return path stored in DB
            return "/uploads/managers/" + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload "
                            + documentType
                            + " file",
                    e
            );
        }
    }
}
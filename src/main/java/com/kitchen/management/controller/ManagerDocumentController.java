package com.kitchen.management.controller;

import com.kitchen.management.service.ManagerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/manager-documents")
public class ManagerDocumentController {

    @Autowired
    private ManagerDocumentService managerDocumentService;

    @PostMapping("/upload/{managerId}")
    public Map<String, String> uploadDocuments(
            @PathVariable Long managerId,
            @RequestParam(value = "aadharCard", required = false) MultipartFile aadharCard,
            @RequestParam(value = "panCard", required = false) MultipartFile panCard,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        return managerDocumentService.uploadDocuments(
                managerId,
                aadharCard,
                panCard,
                photo
        );
    }
}
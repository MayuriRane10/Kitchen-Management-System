
package com.kitchen.management.controller;

import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.service.CloudKitchenService;
import com.kitchen.management.service.ManagerService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cloud-kitchen")
public class CloudKitchenController {

    private final CloudKitchenService cloudKitchenService;
    private final ManagerService managerService;

    public CloudKitchenController(
            CloudKitchenService cloudKitchenService,
            ManagerService managerService) {
        this.cloudKitchenService = cloudKitchenService;
        this.managerService = managerService;
    }

    // Get only the cloud kitchen assigned to the logged-in manager
    @GetMapping("/getdatacloudkitchen")
    public ResponseEntity<CloudKitchen> getDataCloudKitchen(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).build();
        }

        String managerEmail = authentication.getName();

        CloudKitchen cloudKitchen =
                managerService.getCloudKitchenForLoggedInManager(
                        managerEmail);

        return ResponseEntity.ok(cloudKitchen);
    }
}

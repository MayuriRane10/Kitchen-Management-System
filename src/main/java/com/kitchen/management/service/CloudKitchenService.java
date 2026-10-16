package com.kitchen.management.service;

import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.repository.CloudKitchenRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CloudKitchenService {

    private final CloudKitchenRepository cloudKitchenRepository;

    public CloudKitchenService(
            CloudKitchenRepository cloudKitchenRepository) {

        this.cloudKitchenRepository = cloudKitchenRepository;
    }

    // CREATE CLOUD KITCHEN

    public CloudKitchen saveCloudKitchen(
            CloudKitchen cloudKitchen) {

        if (cloudKitchen.getIsActive() == null) {
            cloudKitchen.setIsActive(true);
        }

        if (cloudKitchen.getIsDeleted() == null) {
            cloudKitchen.setIsDeleted(false);
        }

        if (cloudKitchen.getIsOpened() == null) {
            cloudKitchen.setIsOpened(false);
        }

        return cloudKitchenRepository.save(cloudKitchen);
    }

    // GET ALL CLOUD KITCHENS

    public List<CloudKitchen> getAllCloudKitchen() {

        return cloudKitchenRepository.findAll();
    }

    // GET CLOUD KITCHEN BY ID

    public CloudKitchen getCloudKitchenById(
            Long kitchenId) {

        return cloudKitchenRepository.findById(kitchenId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cloud Kitchen not found with ID: "
                                        + kitchenId
                        )
                );
    }

    // UPDATE CLOUD KITCHEN

    public CloudKitchen updateCloudKitchen(
            Long kitchenId,
            CloudKitchen updatedKitchen) {

        CloudKitchen existingKitchen =
                cloudKitchenRepository.findById(kitchenId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cloud Kitchen not found with ID: "
                                                + kitchenId
                                )
                        );

        existingKitchen.setState(
                updatedKitchen.getState()
        );

        existingKitchen.setCity(
                updatedKitchen.getCity()
        );

        existingKitchen.setDivision(
                updatedKitchen.getDivision()
        );

        existingKitchen.setAddress(
                updatedKitchen.getAddress()
        );

        existingKitchen.setPincode(
                updatedKitchen.getPincode()
        );

        if (updatedKitchen.getIsActive() != null) {
            existingKitchen.setIsActive(
                    updatedKitchen.getIsActive()
            );
        }

        if (updatedKitchen.getIsOpened() != null) {
            existingKitchen.setIsOpened(
                    updatedKitchen.getIsOpened()
            );
        }

        return cloudKitchenRepository.save(
                existingKitchen
        );
    }

    // DELETE CLOUD KITCHEN

    public void deleteCloudKitchen(
            Long kitchenId) {

        CloudKitchen cloudKitchen =
                cloudKitchenRepository.findById(kitchenId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cloud Kitchen not found with ID: "
                                                + kitchenId
                                )
                        );

        cloudKitchen.setIsDeleted(true);
        cloudKitchen.setIsActive(false);

        cloudKitchenRepository.save(
                cloudKitchen
        );
    }

    // SEARCH CLOUD KITCHENS BY STATE, CITY, DIVISION

    public List<CloudKitchen> searchCloudKitchens(
            String state,
            String city,
            String division) {

        return cloudKitchenRepository.searchCloudKitchens(
                state,
                city,
                division
        );
    }
}
package com.kitchen.management.controller;

import com.kitchen.management.dto.DeliveryPersonChangePasswordDTO;
import com.kitchen.management.dto.DeliveryPersonLoginDTO;
import com.kitchen.management.dto.DeliveryPersonRequestDTO;
import com.kitchen.management.dto.DeliveryPersonResponseDTO;
import com.kitchen.management.service.DeliveryPersonService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-person")
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(
            DeliveryPersonService deliveryPersonService) {

        this.deliveryPersonService = deliveryPersonService;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<DeliveryPersonResponseDTO> login(
            @RequestBody DeliveryPersonLoginDTO request) {

        return ResponseEntity.ok(
                deliveryPersonService.login(request)
        );
    }

    // SAVE DELIVERY PERSON
    @PostMapping(
            value = "/saveDeliveryPerson",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DeliveryPersonResponseDTO> saveDeliveryPerson(
            @ModelAttribute DeliveryPersonRequestDTO request) {

        return ResponseEntity.ok(
                deliveryPersonService.saveDeliveryPerson(request)
        );
    }

    // GET ALL DELIVERY PERSONS
    @GetMapping("/getAllDeliveryPersons")
    public ResponseEntity<List<DeliveryPersonResponseDTO>>
    getAllDeliveryPersons() {

        return ResponseEntity.ok(
                deliveryPersonService.getAllDeliveryPersons()
        );
    }

    // GET DELIVERY PERSON BY ID
    @GetMapping("/getDeliveryPersonById/{deliveryPersonId}")
    public ResponseEntity<DeliveryPersonResponseDTO>
    getDeliveryPersonById(
            @PathVariable Long deliveryPersonId) {

        return ResponseEntity.ok(
                deliveryPersonService.getDeliveryPersonById(
                        deliveryPersonId
                )
        );
    }

    // UPDATE DELIVERY PERSON
    @PutMapping(
            value = "/updateDeliveryPerson/{deliveryPersonId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DeliveryPersonResponseDTO>
    updateDeliveryPerson(
            @PathVariable Long deliveryPersonId,
            @ModelAttribute DeliveryPersonRequestDTO request) {

        return ResponseEntity.ok(
                deliveryPersonService.updateDeliveryPerson(
                        deliveryPersonId,
                        request
                )
        );
    }

    // DELETE DELIVERY PERSON
    @DeleteMapping("/deleteDeliveryPerson/{deliveryPersonId}")
    public ResponseEntity<String> deleteDeliveryPerson(
            @PathVariable Long deliveryPersonId) {

        return ResponseEntity.ok(
                deliveryPersonService.deleteDeliveryPerson(
                        deliveryPersonId
                )
        );
    }

    // CHANGE PASSWORD
    @PutMapping("/changePassword/{deliveryPersonId}")
    public ResponseEntity<String> changePassword(
            @PathVariable Long deliveryPersonId,
            @RequestBody DeliveryPersonChangePasswordDTO request) {

        return ResponseEntity.ok(
                deliveryPersonService.changePassword(
                        deliveryPersonId,
                        request
                )
        );
    }
}
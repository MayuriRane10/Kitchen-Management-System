package com.kitchen.management.repository;

import com.kitchen.management.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryPersonRepository
        extends JpaRepository<DeliveryPerson, Long> {

    Optional<DeliveryPerson> findByEmail(String email);

    List<DeliveryPerson> findByIsDeletedFalse();
}
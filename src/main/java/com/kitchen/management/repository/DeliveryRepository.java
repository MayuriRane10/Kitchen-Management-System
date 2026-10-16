package com.kitchen.management.repository;

import com.kitchen.management.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository
        extends JpaRepository<Delivery, Long> {
}
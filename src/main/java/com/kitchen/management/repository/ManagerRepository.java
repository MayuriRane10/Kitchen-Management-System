
package com.kitchen.management.repository;

import com.kitchen.management.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    // Find manager by email
    Optional<Manager> findByEmail(String email);

    // Check whether email already exists
    boolean existsByEmail(String email);

    // Find manager assigned to a particular cloud kitchen
    Optional<Manager> findByCloudKitchen_CloudKitchenId(Long cloudKitchenId);
}

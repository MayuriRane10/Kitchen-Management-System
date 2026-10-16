
package com.kitchen.management.repository;

import com.kitchen.management.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("""
        SELECT m
        FROM Meal m
        JOIN m.cuisine c
        WHERE c.cloudKitchen.cloudKitchenId = :cloudKitchenId
          AND m.isAvailable = true
          AND c.isAvailable = true
          AND c.isUnavailable = false
        """)
    List<Meal> findAvailableMealsByCloudKitchenId(
            @Param("cloudKitchenId") Long cloudKitchenId);
}

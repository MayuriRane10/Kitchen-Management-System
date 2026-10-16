package com.kitchen.management.repository;

import com.kitchen.management.entity.CloudKitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CloudKitchenRepository extends JpaRepository<CloudKitchen, Long> {

    List<CloudKitchen> findByIsDeletedFalse();

    List<CloudKitchen> findByIsDeletedFalseAndIsOpened(Boolean isOpened);

    @Query("""
        SELECT c
        FROM CloudKitchen c
        WHERE (c.isDeleted = false OR c.isDeleted IS NULL)
        AND (:state IS NULL OR :state = ''
             OR LOWER(c.state) LIKE LOWER(CONCAT('%', :state, '%')))
        AND (:city IS NULL OR :city = ''
             OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%')))
        AND (:division IS NULL OR :division = ''
             OR LOWER(c.division) LIKE LOWER(CONCAT('%', :division, '%')))
        """)
    List<CloudKitchen> searchCloudKitchens(
            @Param("state") String state,
            @Param("city") String city,
            @Param("division") String division
    );
}
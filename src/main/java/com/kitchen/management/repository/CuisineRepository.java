
package com.kitchen.management.repository;

import com.kitchen.management.entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineRepository
        extends JpaRepository<Cuisine, Long> {
}

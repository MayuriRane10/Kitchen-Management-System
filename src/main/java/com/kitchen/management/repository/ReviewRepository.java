package com.kitchen.management.repository;

import com.kitchen.management.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCloudKitchenCloudKitchenId(Long cloudKitchenId);
}
package com.kitchen.management.controller;

import com.kitchen.management.entity.Review;
import com.kitchen.management.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/kitchen/{cloudKitchenId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long cloudKitchenId,
            @RequestBody Review review) {

        return ResponseEntity.ok(
                reviewService.createReview(cloudKitchenId, review)
        );
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {

        return ResponseEntity.ok(
                reviewService.getAllReviews()
        );
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(
                reviewService.getReviewById(reviewId)
        );
    }

    @GetMapping("/kitchen/{cloudKitchenId}")
    public ResponseEntity<List<Review>> getReviewsByKitchen(
            @PathVariable Long cloudKitchenId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByKitchen(cloudKitchenId)
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody Review review) {

        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, review)
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok("Review deleted successfully");
    }
}
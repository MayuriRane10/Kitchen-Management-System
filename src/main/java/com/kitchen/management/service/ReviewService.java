package com.kitchen.management.service;

import com.kitchen.management.entity.CloudKitchen;
import com.kitchen.management.entity.Review;
import com.kitchen.management.repository.CloudKitchenRepository;
import com.kitchen.management.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CloudKitchenRepository cloudKitchenRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            CloudKitchenRepository cloudKitchenRepository) {

        this.reviewRepository = reviewRepository;
        this.cloudKitchenRepository = cloudKitchenRepository;
    }

    public Review createReview(Long cloudKitchenId, Review review) {

        CloudKitchen cloudKitchen =
                cloudKitchenRepository.findById(cloudKitchenId)
                        .orElseThrow(() ->
                                new RuntimeException("Cloud Kitchen not found"));

        review.setCloudKitchen(cloudKitchen);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByKitchen(Long cloudKitchenId) {
        return reviewRepository
                .findByCloudKitchenCloudKitchenId(cloudKitchenId);
    }

    public Review getReviewById(Long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));
    }

    public Review updateReview(Long reviewId, Review updatedReview) {

        Review existingReview = getReviewById(reviewId);

        existingReview.setReviewerName(updatedReview.getReviewerName());
        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());

        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long reviewId) {

        Review review = getReviewById(reviewId);

        reviewRepository.delete(review);
    }
}
package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponse;
import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.entity.Review;
import com.app.retailcontrol.repository.ReviewRepository;
import com.app.retailcontrol.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<ApiResponse<Object>> getReviews(@PathVariable Long storeId, @PathVariable Long productId) {
        ApiResponse<Object> apiResponse;
        List<ReviewDTO> reviews = reviewService.getReviewsDto(storeId, productId);
        apiResponse = new ApiResponse<>(
                "Reviews retrieved successfully",
                "ok",
                200,
                reviews
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addReview(@RequestBody Review review) {
        ApiResponse<Object> apiResponse;
        reviewRepository.save(review);
        apiResponse = new ApiResponse<>(
                "Product saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

}

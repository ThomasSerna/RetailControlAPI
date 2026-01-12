package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponseDTO;
import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.entity.Review;
import com.app.retailcontrol.repository.ReviewRepository;
import com.app.retailcontrol.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponseDTO<Object>> getReviews(@PathVariable Long storeId, @PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsDto(storeId, productId);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Reviews retrieved successfully",
                "ok",
                200,
                reviews
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Object>> addReview(@RequestBody Review review) {
        reviewRepository.save(review);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Product saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
    }

}

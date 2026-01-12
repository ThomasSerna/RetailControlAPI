package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable Long storeId, @PathVariable Long productId) {
        Map<String, Object> apiResponse = new HashMap<>();
        List<ReviewDTO> reviews = reviewService.getReviewsDto(storeId, productId);
        apiResponse.put("reviews", reviews);
        return apiResponse;
    }

}

package com.app.retailcontrol.service;

import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.entity.Customer;
import com.app.retailcontrol.entity.Review;
import com.app.retailcontrol.repository.CustomerRepository;
import com.app.retailcontrol.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(CustomerRepository customerRepository, ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDTO> getReviewsDto(Long storeId, Long productId) {
        List<Review> reviews = reviewRepository.findAllByStoreIDAndProductID(storeId, productId);

        Set<Long> customerIds = reviews.stream()
                .map(Review::getCustomerID)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, String> customerNameById = customerRepository.findAllById(customerIds).stream()
                .collect(Collectors.toMap(Customer::getId, Customer::getName));

        return reviews.stream()
                .map(r -> new ReviewDTO(
                        customerNameById.getOrDefault(r.getCustomerID(), "Unknown"),
                        r.getComment(),
                        r.getRating()
                )).toList();
    }

}

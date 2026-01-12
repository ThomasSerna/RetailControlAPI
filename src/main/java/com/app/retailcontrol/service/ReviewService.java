package com.app.retailcontrol.service;

import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.entity.Customer;
import com.app.retailcontrol.entity.Review;
import com.app.retailcontrol.repository.CustomerRepository;
import com.app.retailcontrol.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(CustomerRepository customerRepository, ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDTO> getReviewsDto(Long storeId, Long productId){
        List<Review> reviews = reviewRepository.findAllByStoreIDAndProductID(storeId, productId);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        String customerName;

        for (Review review : reviews) {
            customerName = customerRepository.findById(review.getCustomerID())
                    .map(Customer::getName)
                    .orElse(null);

            reviewDTOs.add(new ReviewDTO(
                    customerName == null ? "Unknown" : customerName,
                    review.getComment(),
                    review.getRating()
            ));
        }

        return reviewDTOs;
    }

}

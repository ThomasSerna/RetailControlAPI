package com.app.retailcontrol.service;

import com.app.retailcontrol.dto.ReviewDTO;
import com.app.retailcontrol.entity.Customer;
import com.app.retailcontrol.entity.Inventory;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.entity.Review;
import com.app.retailcontrol.repository.CustomerRepository;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;


    public ServiceClass(ProductRepository productRepository, InventoryRepository inventoryRepository, ReviewRepository reviewRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
    }

    public boolean validateInventory(Inventory inventory){
        return !inventoryRepository.existsByProduct_IdAndStore_Id(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }

    public boolean validateProduct(Product product){
        return !productRepository.existsByName(product.getName());
    }

    public boolean validateProductId(Long id){
        return !productRepository.existsById(id);
    }

    public Optional<Inventory> getInventory(Inventory inventory){
        return inventoryRepository.findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );
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

package com.app.retailcontrol.repository;

import com.app.retailcontrol.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findAllByStoreIDAndProductID(Long storeID, Long productID);
}

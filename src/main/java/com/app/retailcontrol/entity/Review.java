package com.app.retailcontrol.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotNull(message = "Customer cannot be null")
    private Long customerID;

    @NotNull(message = "Store cannot be null")
    private Long storeID;

    @NotNull(message = "Product cannot be null")
    private Long productID;

    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    private String comment;

    public Review(){}

    public Review(Long customerID, Long storeID, Long productID, Integer rating) {
        this.customerID = customerID;
        this.storeID = storeID;
        this.productID = productID;
        this.rating = rating;
    }

    public Review(Long customerID, Long storeID, Long productID, Integer rating, String comment) {
        this.customerID = customerID;
        this.storeID = storeID;
        this.productID = productID;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

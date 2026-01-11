package com.app.retailcontrol.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Name cannot be empty")
    private String category;

    @NotNull(message = "Name cannot be empty")
    private Double price;

    @NotNull(message = "Name cannot be empty")
    @Column(nullable = false, unique = true)
    private String sku;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventories;

    public Product(){}

    public Product(String name, String category, Double price, String sku, List<Inventory> inventories) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.sku = sku;
        this.inventories = inventories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}

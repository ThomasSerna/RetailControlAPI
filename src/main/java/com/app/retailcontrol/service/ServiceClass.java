package com.app.retailcontrol.service;

import com.app.retailcontrol.entity.Inventory;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ServiceClass(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
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
        return productRepository.existsById(id);
    }

    public Inventory getInventoryId(Inventory inventory){
        return inventoryRepository
                .findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                ).orElseThrow(() -> new RuntimeException());
    }



}

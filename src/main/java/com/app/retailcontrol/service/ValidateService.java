package com.app.retailcontrol.service;

import com.app.retailcontrol.entity.Inventory;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.exception.ResourceNotFoundException;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class ValidateService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;

    public ValidateService(ProductRepository productRepository, InventoryRepository inventoryRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
    }

    public boolean inventoryExists(Inventory inventory){
        return inventoryRepository.existsByProduct_IdAndStore_Id(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }

    public boolean storeExistsById(Long storeId) {
        return storeRepository.existsById(storeId);
    }

    public boolean productExists(Product product){
        return productRepository.existsByName(product.getName());
    }

    public boolean productByIdExists(Long id){
        return productRepository.existsById(id);
    }

    public boolean validateQuantity(Integer quantity, Long storeId, Long productId){
        Inventory inventory = inventoryRepository
                .findByProduct_IdAndStore_Id(productId, storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory doesn't exists"));
        return inventory.getStock() >= quantity;
    }

    public Optional<Inventory> getInventory(Inventory inventory){
        return inventoryRepository.findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );
    }

}

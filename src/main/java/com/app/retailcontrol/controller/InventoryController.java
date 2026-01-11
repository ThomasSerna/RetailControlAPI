package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.CombinedRequest;
import com.app.retailcontrol.entity.Inventory;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.service.ServiceClass;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {


    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ServiceClass serviceClass;

    public InventoryController(ProductRepository productRepository, InventoryRepository inventoryRepository, ServiceClass serviceClass) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.serviceClass = serviceClass;
    }

    @PutMapping
    public Map<String, String> updateInventory(CombinedRequest combinedRequest){
        Map<String, String> apiResponse = new HashMap<>();
        Product product = combinedRequest.getProduct();
        Inventory inventory = combinedRequest.getInventory();

        if (serviceClass.validateProductId(product.getId())){
            throw new RuntimeException("Product doesn't exists");
        }

        Optional<Inventory> dbInventory = serviceClass.getInventory(inventory);

        if (dbInventory.isEmpty()){
            apiResponse.put("message", "No data available");
            return apiResponse;
        } else {
            Inventory newInventory = dbInventory.get();
            newInventory.setStock(inventory.getStock());
            inventoryRepository.save(newInventory);

            apiResponse.put("message", "Successfully updated product");
            return apiResponse;
        }
    }

    @PostMapping
    public Map<String, String> saveInventory(Inventory inventory){
        Map<String, String> apiResponse = new HashMap<>();

        if (serviceClass.validateInventory(inventory)){
            inventoryRepository.save(inventory);

            apiResponse.put("message", "Data saved successfully");
            return apiResponse;
        }

        apiResponse.put("message", "Data is already present");
        return apiResponse;
    }

    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> products = productRepository.findAllByStoreId(storeId);

        apiResponse.put("products", products);
        return apiResponse;
    }

    @GetMapping("/filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable Long storeId){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> products;
        if (category == null) {
            products = productRepository.findAllByNameIgnoreCase(name);
        } else if (name == null) {
            products = productRepository.findAllByCategoryAndStoreId(storeId, category);
        } else {
            products = productRepository.findAllByNameAndCategory(storeId, name, category);
        }

        apiResponse.put("product", products);
        return apiResponse;
    }

    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> products = productRepository.findByNameLike(storeId, name);

        apiResponse.put("product", products);
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id){
        Map<String, String> apiResponse = new HashMap<>();
        if (serviceClass.validateProductId(id)) {
            apiResponse.put("message", "The product doesn't exists");
            return apiResponse;
        }

        productRepository.deleteById(id);
        apiResponse.put("message", "The product was deleted");
        return apiResponse;
    }

    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable Integer quantity, @PathVariable Long storeId, @PathVariable Long productId){
        Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId).orElseThrow(() -> new RuntimeException("Product doesn't exists"));
        return inventory.getStock() >= quantity;
    }

}

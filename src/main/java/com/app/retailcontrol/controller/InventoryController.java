package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponse;
import com.app.retailcontrol.dto.CombinedRequest;
import com.app.retailcontrol.entity.Inventory;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.exception.ResourceAlreadyExistsException;
import com.app.retailcontrol.exception.ResourceNotFoundException;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.service.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ValidateService validateService;

    public InventoryController(ProductRepository productRepository, InventoryRepository inventoryRepository, ValidateService validateService) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.validateService = validateService;
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Object>> updateInventory(@RequestBody CombinedRequest combinedRequest){
        ApiResponse<Object> apiResponse;
        Product product = combinedRequest.getProduct();
        Inventory inventory = combinedRequest.getInventory();

        if (!validateService.productByIdExists(product.getId())){
            throw new ResourceNotFoundException("Product doesn't exists");
        }

        Optional<Inventory> dbInventory = validateService.getInventory(inventory);

        if (dbInventory.isEmpty()){
            throw new ResourceNotFoundException("Inventory doesn't exists");
        } else {
            Inventory newInventory = dbInventory.get();
            newInventory.setStock(inventory.getStock());
            inventoryRepository.save(newInventory);

            apiResponse = new ApiResponse<>(
                    "Inventory updated successfully",
                    "ok",
                    200,
                    null
            );

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> saveInventory(@RequestBody Inventory inventory){
        ApiResponse<Object> apiResponse;

        if (!validateService.inventoryExists(inventory)){
            inventoryRepository.save(inventory);

            apiResponse = new ApiResponse<>(
                    "Inventory saved successfully",
                    "created",
                    201,
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }

        throw new ResourceAlreadyExistsException("Inventory already exists");
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Object>> getAllProducts(@PathVariable Long storeId){
        ApiResponse<Object> apiResponse;
        List<Product> products = productRepository.findAllByStoreId(storeId);

        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // This has to change to param filter
    @GetMapping("/filter/{category}/{name}/{storeId}")
    public ResponseEntity<ApiResponse<Object>> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable Long storeId){
        ApiResponse<Object> apiResponse;
        List<Product> products;
        if (category == null) {
            products = productRepository.findAllByNameIgnoreCase(name);
        } else if (name == null) {
            products = productRepository.findAllByCategoryAndStoreId(storeId, category);
        } else {
            products = productRepository.findAllByNameAndCategory(storeId, name, category);
        }

        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Also has to change to param filter
    @GetMapping("/search/{name}/{storeId}")
    public ResponseEntity<ApiResponse<Object>> searchProduct(@PathVariable String name, @PathVariable Long storeId){
        ApiResponse<Object> apiResponse;
        List<Product> products = productRepository.findByNameLike(storeId, name);

        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // has also to change
    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public ResponseEntity<ApiResponse<Object>> validateQuantity(@PathVariable Integer quantity, @PathVariable Long storeId, @PathVariable Long productId){
        Boolean isQuantityEnough = validateService.validateQuantity(quantity, storeId, productId);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                isQuantityEnough ? "Stock is sufficient" : "Insufficient stock",
                "ok",
                200,
                isQuantityEnough
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}

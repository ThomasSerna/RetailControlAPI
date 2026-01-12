package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponseDTO;
import com.app.retailcontrol.dto.CombinedRequestDTO;
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

import java.util.List;
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
    public ResponseEntity<ApiResponseDTO<Object>> updateInventory(@RequestBody CombinedRequestDTO combinedRequestDTO){
        Product product = combinedRequestDTO.getProduct();
        Inventory inventory = combinedRequestDTO.getInventory();

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

            ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                    "Inventory updated successfully",
                    "ok",
                    200,
                    null
            );

            return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Object>> saveInventory(@RequestBody Inventory inventory){
        if (!validateService.inventoryExists(inventory)){
            inventoryRepository.save(inventory);

            ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                    "Inventory saved successfully",
                    "created",
                    201,
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
        }

        throw new ResourceAlreadyExistsException("Inventory already exists");
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponseDTO<Object>> getProducts(
            @PathVariable Long storeId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        List<Product> products;

        if (!hasName && !hasCategory) {
            products = productRepository.findAllByStoreId(storeId);
        } else if (hasName && !hasCategory) {
            products = productRepository.findByNameLike(storeId, name);
        } else if (!hasName && hasCategory) {
            products = productRepository.findAllByCategoryAndStoreId(storeId, category);
        } else {
            products = productRepository.findAllByNameAndCategory(storeId, name, category);
        }

        ApiResponseDTO<Object> body = new ApiResponseDTO<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.ok(body);
    }


    @GetMapping("/search/{storeId}")
    public ResponseEntity<ApiResponseDTO<Object>> searchProduct(@RequestParam String name, @PathVariable Long storeId){
        List<Product> products = productRepository.findByNameLike(storeId, name);

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @GetMapping("/validate/{storeId}/{productId}")
    public ResponseEntity<ApiResponseDTO<Object>> validateQuantity(@RequestParam Integer quantity, @PathVariable Long storeId, @PathVariable Long productId){
        Boolean isQuantityEnough = validateService.validateQuantity(quantity, storeId, productId);

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                isQuantityEnough ? "Stock is sufficient" : "Insufficient stock",
                "ok",
                200,
                isQuantityEnough
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

}

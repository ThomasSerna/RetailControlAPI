package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponse;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.exception.ResourceAlreadyExistsException;
import com.app.retailcontrol.exception.ResourceNotFoundException;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.service.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ValidateService validateService;

    public ProductController(ProductRepository productRepository, ValidateService validateService) {
        this.productRepository = productRepository;
        this.validateService = validateService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addProduct(@RequestBody Product product) {
        ApiResponse<Object> apiResponse;
        if (validateService.productExists(product)){
            productRepository.save(product);

            apiResponse = new ApiResponse<>(
                    "Product saved successfully",
                    "created",
                    201,
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }

        throw new ResourceAlreadyExistsException("Product already exists");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getProductById(@PathVariable Long id) {
        ApiResponse<Object> apiResponse;
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        apiResponse = new ApiResponse<>(
                "Product retrieved successfully",
                "ok",
                200,
                product
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Object>> updateProduct(@RequestBody Product product) {
        ApiResponse<Object> apiResponse;
        productRepository.save(product);
        apiResponse = new ApiResponse<>(
                "Product updated successfully",
                "ok",
                200,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllProducts(){
        ApiResponse<Object> apiResponse;
        List<Product> products = productRepository.findAll();

        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Has to change to param filter
    @GetMapping("/category/{name}/{category}")
    public ResponseEntity<ApiResponse<Object>> filterByCategoryProduct(@PathVariable String name, @PathVariable String category){
        ApiResponse<Object> apiResponse;
        List<Product> products;
        if (category == null) {
            products = productRepository.findAllByNameIgnoreCase(name);
        } else if (name == null) {
            products = productRepository.findAllByCategory(category);
        } else {
            products = productRepository.findAllByNameContainingIgnoreCaseAndCategory(name, category);
        }

        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Also has to change
    @GetMapping("filter/{category}/{storeId}")
    public ResponseEntity<ApiResponse<Object>> getProductByCategoryAndStoreId(@PathVariable String category, @PathVariable Long storeId){
        ApiResponse<Object> apiResponse;
        List<Product> products = productRepository.findAllByCategoryAndStoreId(storeId, category);
        apiResponse = new ApiResponse<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable Long id){
        ApiResponse<Object> apiResponse;
        if (validateService.productByIdExists(id)) {
            throw new ResourceNotFoundException("Product doesn't exists");
        }
        productRepository.deleteById(id);

        apiResponse = new ApiResponse<>(
                "Product deleted successfully",
                "ok",
                204,
                null
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<ApiResponse<Object>> searchProduct(@PathVariable String name){
        ApiResponse<Object> apiResponse;
        List<Product> product = productRepository.findAllByNameContainingIgnoreCase(name);
        apiResponse = new ApiResponse<>(
                "Product retrieved successfully",
                "ok",
                200,
                product
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}

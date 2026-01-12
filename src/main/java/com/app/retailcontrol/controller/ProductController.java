package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponseDTO;
import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.exception.ResourceAlreadyExistsException;
import com.app.retailcontrol.exception.ResourceNotFoundException;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.service.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponseDTO<Object>> addProduct(@RequestBody Product product) {
        if (validateService.productExists(product)){
            productRepository.save(product);

            ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                    "Product saved successfully",
                    "created",
                    201,
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
        }

        throw new ResourceAlreadyExistsException("Product already exists");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> getProductById(@PathVariable Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Product retrieved successfully",
                "ok",
                200,
                product
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @PutMapping
    public ResponseEntity<ApiResponseDTO<Object>> updateProduct(@RequestBody Product product) {
        productRepository.save(product);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Product updated successfully",
                "ok",
                200,
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Object>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ){
        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();
        List<Product> products;

        if (!hasName && !hasCategory){
            products = productRepository.findAll();
        } else if (hasName && !hasCategory) {
            products = productRepository.findAllByNameIgnoreCase(name);
        } else if (!hasName && hasCategory) {
            products = productRepository.findAllByCategory(category);
        } else {
            products = productRepository.findAllByNameContainingIgnoreCaseAndCategory(name, category);
        }

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Products retrieved successfully",
                "ok",
                200,
                products
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteProduct(@PathVariable Long id){
        if (validateService.productByIdExists(id)) {
            throw new ResourceNotFoundException("Product doesn't exists");
        }
        productRepository.deleteById(id);

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Product deleted successfully",
                "ok",
                204,
                null
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponseDTO);
    }

    @GetMapping("/searchProduct")
    public ResponseEntity<ApiResponseDTO<Object>> searchProduct(@RequestParam String name){
        List<Product> product = productRepository.findAllByNameContainingIgnoreCase(name);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Product retrieved successfully",
                "ok",
                200,
                product
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

}

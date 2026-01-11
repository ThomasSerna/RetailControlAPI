package com.app.retailcontrol.controller;

import com.app.retailcontrol.entity.Product;
import com.app.retailcontrol.repository.InventoryRepository;
import com.app.retailcontrol.repository.ProductRepository;
import com.app.retailcontrol.service.ServiceClass;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ServiceClass serviceClass;
    private final InventoryRepository inventoryRepository;

    public ProductController(ProductRepository productRepository, ServiceClass serviceClass, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.serviceClass = serviceClass;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> apiResponse = new HashMap<>();
        if (serviceClass.validateProduct(product)){
            productRepository.save(product);
            apiResponse.put("message", "Product added to the database");
            return apiResponse;
        }

        apiResponse.put("message", "Product already exists");
        return apiResponse;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        Map<String, Object> apiResponse = new HashMap<>();
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
        apiResponse.put("products", product);
        return apiResponse;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> apiResponse = new HashMap<>();
        productRepository.save(product);
        apiResponse.put("message", "Product updated");
        return apiResponse;
    }

    @GetMapping
    public Map<String, Object> getAllProducts(){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> product = productRepository.findAll();
        apiResponse.put("products", product);
        return apiResponse;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterByCategoryProduct(@PathVariable String name, @PathVariable String category){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> products;
        if (category == null) {
            products = productRepository.findAllByNameIgnoreCase(name);
        } else if (name == null) {
            products = productRepository.findAllByCategory(category);
        } else {
            products = productRepository.findAllByNameContainingIgnoreCaseAndCategory(name, category);
        }
        apiResponse.put("products", products);
        return apiResponse;
    }

    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object> getProductByCategoryAndStoreId(@PathVariable String category, @PathVariable Long storeid){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> product = productRepository.findAllByCategoryAndStoreId(storeid, category);
        apiResponse.put("products", product);
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

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name){
        Map<String, Object> apiResponse = new HashMap<>();
        List<Product> product = productRepository.findAllByNameContainingIgnoreCase(name);
        apiResponse.put("products", product);
        return apiResponse;
    }

}

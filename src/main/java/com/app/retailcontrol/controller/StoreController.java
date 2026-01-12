package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.PlaceOrderRequestDTO;
import com.app.retailcontrol.entity.Store;
import com.app.retailcontrol.repository.StoreRepository;
import com.app.retailcontrol.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {


    private final StoreRepository storeRepository;
    private final OrderService orderService;

    public StoreController(StoreRepository storeRepository, OrderService orderService) {
        this.storeRepository = storeRepository;
        this.orderService = orderService;
    }

    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> apiResponse = new HashMap<>();
        storeRepository.save(store);
        apiResponse.put("message", "Store saved successfully. ID: " + store.getId());
        return apiResponse;
    }

    @GetMapping("/validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId){
        return storeRepository.existsById(storeId);
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        Map<String, String> apiResponse = new HashMap<>();
        orderService.saveOrder(placeOrderRequestDTO);
        apiResponse.put("message", "Order placed successfully");
        return apiResponse;
    }
}

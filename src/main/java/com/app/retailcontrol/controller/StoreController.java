package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponse;
import com.app.retailcontrol.dto.PlaceOrderRequestDTO;
import com.app.retailcontrol.entity.Store;
import com.app.retailcontrol.repository.StoreRepository;
import com.app.retailcontrol.service.OrderService;
import com.app.retailcontrol.service.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {


    private final StoreRepository storeRepository;
    private final OrderService orderService;
    private final ValidateService validateService;

    public StoreController(StoreRepository storeRepository, OrderService orderService, ValidateService validateService) {
        this.storeRepository = storeRepository;
        this.orderService = orderService;
        this.validateService = validateService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addStore(@RequestBody Store store) {
        ApiResponse<Object> apiResponse;
        storeRepository.save(store);
        apiResponse = new ApiResponse<>(
                "Store saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/validate/{storeId}")
    public ResponseEntity<ApiResponse<Object>> validateStore(@PathVariable Long storeId){
        Boolean storeExists = validateService.storeExistsById(storeId);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                storeExists ? "Store exists" : "Store doesn't exists",
                "ok",
                200,
                storeExists
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ApiResponse<Object>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        ApiResponse<Object> apiResponse;
        orderService.saveOrder(placeOrderRequestDTO);
        apiResponse = new ApiResponse<>(
                "Order saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}

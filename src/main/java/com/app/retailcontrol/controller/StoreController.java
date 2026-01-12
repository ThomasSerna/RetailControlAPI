package com.app.retailcontrol.controller;

import com.app.retailcontrol.dto.ApiResponseDTO;
import com.app.retailcontrol.dto.PlaceOrderRequestDTO;
import com.app.retailcontrol.entity.Store;
import com.app.retailcontrol.repository.StoreRepository;
import com.app.retailcontrol.service.OrderService;
import com.app.retailcontrol.service.ValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponseDTO<Object>> addStore(@RequestBody Store store) {
        storeRepository.save(store);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Store saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
    }

    @GetMapping("/validate/{storeId}")
    public ResponseEntity<ApiResponseDTO<Object>> validateStore(@PathVariable Long storeId){
        Boolean storeExists = validateService.storeExistsById(storeId);

        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                storeExists ? "Store exists" : "Store doesn't exists",
                "ok",
                200,
                storeExists
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ApiResponseDTO<Object>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {
        orderService.saveOrder(placeOrderRequestDTO);
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Order saved successfully",
                "created",
                201,
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
    }
}

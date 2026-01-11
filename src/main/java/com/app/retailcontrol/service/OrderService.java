package com.app.retailcontrol.service;

import com.app.retailcontrol.dto.PlaceOrderRequestDTO;
import com.app.retailcontrol.dto.PurchaseProductDTO;
import com.app.retailcontrol.entity.*;
import com.app.retailcontrol.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreRepository storeRepository;

    public OrderService(CustomerRepository customerRepository, InventoryRepository inventoryRepository, OrderDetailsRepository orderDetailsRepository, OrderItemRepository orderItemRepository, StoreRepository storeRepository) {
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemRepository = orderItemRepository;
        this.storeRepository = storeRepository;
    }

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequestDTO){
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(placeOrderRequestDTO.getCustomerEmail());
        Customer customer;

        if (optionalCustomer.isEmpty()){
            customer = new Customer(
                    placeOrderRequestDTO.getCustomerName(),
                    placeOrderRequestDTO.getCustomerEmail(),
                    placeOrderRequestDTO.getCustomerPhone()
            );
            customerRepository.save(customer);
        } else {
            customer = optionalCustomer.get();
        }

        Store store = storeRepository
                .findById(placeOrderRequestDTO.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found. Id provided: " + placeOrderRequestDTO.getStoreId()));

        OrderDetails orderDetails = new OrderDetails(
                customer,
                store,
                placeOrderRequestDTO.getTotalPrice()
        );
        orderDetailsRepository.save(orderDetails);

        Inventory inventory;
        OrderItem orderItem;
        for (PurchaseProductDTO purchaseProductDTO : placeOrderRequestDTO.getPurchaseProducts()){
            inventory = inventoryRepository
                    .findByProduct_IdAndStore_Id(purchaseProductDTO.getId(), store.getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            orderItem = new OrderItem(
                    orderDetails,
                    inventory.getProduct(),
                    purchaseProductDTO.getQuantity(),
                    purchaseProductDTO.getPrice()
            );

            int newStock = inventory.getStock() - purchaseProductDTO.getQuantity();
            inventory.setStock(newStock);

            inventoryRepository.save(inventory);
            orderItemRepository.save(orderItem);
        }
    }

}

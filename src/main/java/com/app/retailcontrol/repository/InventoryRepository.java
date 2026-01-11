package com.app.retailcontrol.repository;

import com.app.retailcontrol.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct_IdAndStore_Id(Long productId, Long storeId);
    Boolean existsByProduct_IdAndStore_Id(Long productId, Long storeId);
    List<Inventory> findAllByStore_Id(Long storeId);
    Long deleteByProduct_Id(Long productId);
}

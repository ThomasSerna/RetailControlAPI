package com.app.retailcontrol.repository;

import com.app.retailcontrol.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(String category);
    List<Product> findAllByPriceBetween(Double minPrice, Double maxPrice);
    Optional<Product> findBySku(String sku);
    Optional<Product> findByName(String name);
    List<Product> findAllByNameIgnoreCase(String name);
    List<Product> findAllByNameIgnoreCaseAndCategory(String name, String category);
    List<Product> findAllByNameContainingIgnoreCaseAndCategory(String name, String category);
    Boolean existsByName(String name);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.product.category = :category")
    List<Product> findAllByNameAndCategory(@Param("storeId") Long storeId, @Param("pname") String pname, @Param("category") String category);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    List<Product> findAllByCategoryAndStoreId(@Param("storeId") Long storeId, @Param("category") String category);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    List<Product> findAllByStoreId(@Param("storeId") Long storeId);
}

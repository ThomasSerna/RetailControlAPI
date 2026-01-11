package com.app.retailcontrol.repository;

import com.app.retailcontrol.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByNameContainingIgnoreCase(String name);
}

package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Inventory entities.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Find inventory by userId and ingredientId
    List<Inventory> findByUserIdAndIngredientId(Long userId, Long ingredientId);

    // Find all inventory for a specific user
    List<Inventory> findByUserId(Long userId);
}

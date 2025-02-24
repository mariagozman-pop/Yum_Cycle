package com.example.leftover_rescue.SERVICE;

import com.example.leftover_rescue.MODEL.Inventory;
import com.example.leftover_rescue.REPOSITORY.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling inventory-related operations.
 */
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public List<Inventory> getInventoryByUserAndIngredient(Long userId, Long ingredientId) {
        return inventoryRepository.findByUserIdAndIngredientId(userId, ingredientId);
    }

    public List<Inventory> getAllInventoryForUser(Long userId) {
        return inventoryRepository.findByUserId(userId);
    }

    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        return inventoryRepository.findById(id).map(existingInventory -> {
            // Update the fields of the existing inventory with the new details
            existingInventory.setIngredient(updatedInventory.getIngredient());
            existingInventory.setQuantity(updatedInventory.getQuantity());
            existingInventory.setUser(updatedInventory.getUser());

            // Save the updated inventory object
            return inventoryRepository.save(existingInventory);
        }).orElseThrow(() -> new IllegalArgumentException("Inventory item not found with id: " + id));
    }
}

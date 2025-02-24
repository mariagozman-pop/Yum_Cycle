package com.example.leftover_rescue.CONTROLLER;

import com.example.leftover_rescue.MODEL.Inventory;
import com.example.leftover_rescue.SERVICE.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing inventory.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Get all inventory items
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    // Get an inventory item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        if (inventory != null) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new inventory item
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        try {
            Inventory savedInventory = inventoryService.addInventory(inventory);
            return ResponseEntity.ok(savedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update an existing inventory item
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory updatedInventory) {
        Inventory existingInventory = inventoryService.getInventoryById(id);
        if (existingInventory == null) {
            return ResponseEntity.notFound().build();
        }
        Inventory savedInventory = inventoryService.updateInventory(id, updatedInventory);
        return ResponseEntity.ok(savedInventory);
    }

    // Delete an inventory item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        Inventory existingInventory = inventoryService.getInventoryById(id);
        if (existingInventory == null) {
            return ResponseEntity.notFound().build();
        }
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    // Search inventory by userId and optionally ingredientId
    @GetMapping("/search")
    public ResponseEntity<List<Inventory>> searchInventory(
            @RequestParam Long userId,
            @RequestParam(required = false) Long ingredientId) {

        List<Inventory> inventory;

        if (ingredientId != null) {
            // Search by userId and ingredientId
            inventory = inventoryService.getInventoryByUserAndIngredient(userId, ingredientId);
        } else {
            // Get all inventory for the user
            inventory = inventoryService.getAllInventoryForUser(userId);
        }

        if (inventory.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(inventory);
    }
}
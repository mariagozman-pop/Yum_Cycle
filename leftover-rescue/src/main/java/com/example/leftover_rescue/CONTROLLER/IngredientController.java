package com.example.leftover_rescue.CONTROLLER;

import com.example.leftover_rescue.MODEL.Ingredient;
import com.example.leftover_rescue.SERVICE.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing ingredients.
 */
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // Get all ingredients
    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    // Get an ingredient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient != null) {
            return ResponseEntity.ok(ingredient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new ingredient
    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        Ingredient savedIngredient = ingredientService.addIngredient(ingredient);
        return ResponseEntity.ok(savedIngredient);
    }

    // Update an existing ingredient
    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable Long id,
            @RequestBody Ingredient ingredientDetails) {
        Ingredient updatedIngredient = ingredientService.updateIngredient(id, ingredientDetails);
        if (updatedIngredient != null) {
            return ResponseEntity.ok(updatedIngredient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an ingredient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    // Search ingredients by name
    @GetMapping("/search")
    public ResponseEntity<List<Ingredient>> searchIngredientsByName(@RequestParam String name) {
        List<Ingredient> ingredients = ingredientService.searchIngredientsByName(name);
        return ResponseEntity.ok(ingredients);
    }
}

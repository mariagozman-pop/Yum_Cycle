package com.example.leftover_rescue.CONTROLLER;

import com.example.leftover_rescue.MODEL.RecipeIngredient;
import com.example.leftover_rescue.SERVICE.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing recipe ingredients.
 */
@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @Autowired
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeIngredient>> getAllRecipeIngredients() {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();
        return ResponseEntity.ok(recipeIngredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredient> getRecipeIngredientById(@PathVariable Long id) {
        RecipeIngredient recipeIngredient = recipeIngredientService.getRecipeIngredientById(id);
        if (recipeIngredient != null) {
            return ResponseEntity.ok(recipeIngredient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<RecipeIngredient> createRecipeIngredient(@RequestBody RecipeIngredient recipeIngredient) {
        RecipeIngredient savedRecipeIngredient = recipeIngredientService.addRecipeIngredient(recipeIngredient);
        return ResponseEntity.ok(savedRecipeIngredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable Long id) {
        recipeIngredientService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}

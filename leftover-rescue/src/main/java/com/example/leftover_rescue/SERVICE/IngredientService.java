package com.example.leftover_rescue.SERVICE;

import com.example.leftover_rescue.MODEL.Ingredient;
import com.example.leftover_rescue.REPOSITORY.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling ingredient-related operations.
 */
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredientDetails) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            ingredient.setIngredientName(ingredientDetails.getIngredientName());
            ingredient.setExpiryDate(ingredientDetails.getExpiryDate());
            return ingredientRepository.save(ingredient);
        } else {
            return null; // Handle not found
        }
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public List<Ingredient> searchIngredientsByName(String name) {
        return ingredientRepository.findByIngredientNameContainingIgnoreCase(name);
    }
}

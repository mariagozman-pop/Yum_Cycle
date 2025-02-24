package com.example.leftover_rescue.SERVICE;

import com.example.leftover_rescue.MODEL.RecipeIngredient;
import com.example.leftover_rescue.REPOSITORY.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling recipe-ingredient-related operations.
 */
@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    public RecipeIngredient getRecipeIngredientById(Long id) {
        return recipeIngredientRepository.findById(id).orElse(null);
    }

    public RecipeIngredient addRecipeIngredient(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void deleteRecipeIngredient(Long id) {
        recipeIngredientRepository.deleteById(id);
    }
}

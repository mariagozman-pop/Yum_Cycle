package com.example.leftover_rescue.SERVICE;

import com.example.leftover_rescue.MODEL.Recipe;
import com.example.leftover_rescue.REPOSITORY.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling recipe-related operations.
 */
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe addRecipe(Recipe recipe) {
        if (recipe.getRecipeName() == null || recipe.getRecipeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or empty");
        }
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}

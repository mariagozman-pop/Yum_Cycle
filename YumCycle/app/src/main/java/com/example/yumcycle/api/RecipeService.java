// RecipeService.java
package com.example.yumcycle.api;

import com.example.yumcycle.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecipeService {

    /**
     * Retrieves a list of all recipes.
     *
     * @return A Call object encapsulating a List of Recipe objects.
     */
    @GET("/api/recipes")
    Call<List<Recipe>> getAllRecipes();

    /**
     * Retrieves a single recipe by its ID.
     *
     * @param id The ID of the recipe to retrieve.
     * @return A Call object encapsulating the Recipe object.
     */
    @GET("/api/recipes/{id}")
    Call<Recipe> getRecipeById(@Path("id") Long id);

    /**
     * Creates a new recipe.
     *
     * @param recipe The Recipe object to create.
     * @return A Call object encapsulating the created Recipe object.
     */
    @POST("/api/recipes")
    Call<Recipe> createRecipe(@Body Recipe recipe);

    /**
     * Deletes a recipe by its ID.
     *
     * @param id The ID of the recipe to delete.
     * @return A Call object encapsulating a Void response.
     */
    @DELETE("/api/recipes/{id}")
    Call<Void> deleteRecipe(@Path("id") Long id);
}
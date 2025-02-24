package com.example.yumcycle.api;

import com.example.yumcycle.models.Ingredient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IngredientService {

    // Get all ingredients
    @GET("/api/ingredients")
    Call<List<Ingredient>> getAllIngredients();

    // Get an ingredient by ID
    @GET("/api/ingredients/{id}")
    Call<Ingredient> getIngredientById(@Path("id") Long id);

    // Create a new ingredient
    @POST("/api/ingredients")
    Call<Ingredient> createIngredient(@Body Ingredient ingredient);

    // Update an existing ingredient
    @PUT("/api/ingredients/{id}")
    Call<Ingredient> updateIngredient(
            @Path("id") Long id,
            @Body Ingredient ingredientDetails
    );

    // Delete an ingredient
    @DELETE("/api/ingredients/{id}")
    Call<Void> deleteIngredient(@Path("id") Long id);

    // Search ingredients by name
    @GET("/api/ingredients/search")
    Call<List<Ingredient>> searchIngredientsByName(@Query("name") String name);
}

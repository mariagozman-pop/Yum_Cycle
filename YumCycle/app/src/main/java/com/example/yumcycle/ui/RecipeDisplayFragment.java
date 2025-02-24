package com.example.yumcycle.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.example.yumcycle.R;
import com.example.yumcycle.api.ApiClient;
import com.example.yumcycle.api.InventoryService;
import com.example.yumcycle.models.Favorites;
import com.example.yumcycle.api.FavoritesService;
import com.example.yumcycle.models.Inventory;
import com.example.yumcycle.models.Recipe;
import com.example.yumcycle.api.RecipeService;
import com.example.yumcycle.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;


public class RecipeDisplayFragment extends Fragment {

    private static final String ARG_RECIPE_CONTENT = "recipe_content";
    private FavoritesService favoritesService;
    private RecipeService recipeService;
    private InventoryService inventoryService;
    private List<Inventory> usedIngredients;

    private Long createdRecipeId; // Member variable to store the created Recipe ID

    public static RecipeDisplayFragment newInstance(String recipeContent, List<Inventory> usedIngredients) {
        RecipeDisplayFragment fragment = new RecipeDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_CONTENT, recipeContent);
        Gson gson = new Gson();
        String ingredientsJson = gson.toJson(usedIngredients);
        args.putString("used_ingredients", ingredientsJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeService = ApiClient.getRetrofitInstance().create(RecipeService.class);
        favoritesService = ApiClient.getRetrofitInstance().create(FavoritesService.class);
        inventoryService = ApiClient.getRetrofitInstance().create(InventoryService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind the my account button
        View myAccountButton = view.findViewById(R.id.logoutButton);
        myAccountButton.setOnClickListener(v -> {
            // Navigate to the my account screen
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_RecipeDisplayFragment_to_MyAccountFragment);
        });

        TextView recipeContentTextView = view.findViewById(R.id.recipe_full_text_view);
        recipeContentTextView.setMovementMethod(new android.text.method.ScrollingMovementMethod());

        Button addToFavoritesButton = view.findViewById(R.id.add_to_favorites_button);
        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_RecipeDisplayFragment_to_SearchRecipeFragment);
        });

        // Retrieve the recipe content from arguments
        String recipeContent = getArguments() != null ? getArguments().getString(ARG_RECIPE_CONTENT) : null;
        String ingredientsJson = getArguments() != null ? getArguments().getString("used_ingredients") : null;

        if (ingredientsJson != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Inventory>>() {}.getType();
            usedIngredients = gson.fromJson(ingredientsJson, listType);
        } else {
            usedIngredients = new ArrayList<>();
        }

        if (recipeContent != null) {
            // Display the entire recipe content as-is
            recipeContentTextView.setText(recipeContent);
            addToRecipe(recipeContent);
        } else {
            recipeContentTextView.setText("No recipe content available.");
        }

        // Add to favorites button click listener
        addToFavoritesButton.setOnClickListener(v -> {
            addToFavoritesButton.setEnabled(false);
            if (recipeContent != null) {
                // Retrieve the current user
                User currentUser = getUserFromLocalStorage();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (createdRecipeId != null) {
                    addRecipeToFavorites(currentUser.getId(), createdRecipeId);
                } else {
                    Toast.makeText(getContext(), "Recipe not created yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Adds the recipe to the server.
     *
     * @param recipeContent The content of the recipe to add.
     */
    private void addToRecipe(String recipeContent) {
        // Step 1: Retrieve the current user
        User currentUser = getUserFromLocalStorage();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 2: Create the Recipe entity
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeContent); // Assuming constructor sets content

        // Step 3: Send the Recipe to the server
        recipeService.createRecipe(recipe).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    createdRecipeId = response.body().getId();
                    // Optionally, you can notify the user or update the UI here
                } else {
                    Toast.makeText(getContext(), "Failed to add recipe.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Adds the recipe to the user's favorites.
     *
     * @param userId    The ID of the user.
     * @param recipeId  The ID of the recipe to favorite.
     */
    private void addRecipeToFavorites(int userId, Long recipeId) {
        Favorites favorite = new Favorites();

        // Create and set the User object with the ID
        User user = new User();
        user.setId((long) userId);  // Ensure the type matches (Long)
        favorite.setUser(user);

        // Create and set the Recipe object with the ID
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        favorite.setRecipe(recipe);

        favoritesService.addFavorite(favorite).enqueue(new Callback<Favorites>() {
            @Override
            public void onResponse(Call<Favorites> call, Response<Favorites> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Bon appetit!", Toast.LENGTH_SHORT).show();
                    if (usedIngredients != null && !usedIngredients.isEmpty()) {
                        removeUsedIngredients(user.getId(), usedIngredients);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to process request.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Favorites> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Retrieves the current user from local storage.
     *
     * @return The current User object or null if not found.
     */
    private void removeUsedIngredients(int userId, List<Inventory> ingredientsToRemove) {
        for (Inventory ingredient : ingredientsToRemove) {
            // Fetch current inventory items matching userId and ingredientId
            inventoryService.searchInventory(userId, ingredient.getIngredient().getId()).enqueue(new Callback<List<Inventory>>() {
                @Override
                public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<Inventory> currentInventories = response.body();

                        // Assuming one inventory item per ingredient per user
                        Inventory currentInventory = currentInventories.get(0);
                        // Remove the inventory item
                        inventoryService.deleteInventory(currentInventory.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("InventoryUpdate", "Ingredient removed from inventory.");
                                } else {
                                    Log.e("InventoryUpdate", "Failed to remove ingredient from inventory.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("InventoryUpdate", "Error removing ingredient: " + t.getMessage());
                            }
                        });
                    } else {
                        Log.e("InventoryFetch", "No inventory found for ingredient: " + ingredient.getIngredient().getIngredientName());
                    }
                }

                @Override
                public void onFailure(Call<List<Inventory>> call, Throwable t) {
                    Log.e("InventoryFetch", "Error fetching inventory item: " + t.getMessage());
                }
            });
        }
    }

    private User getUserFromLocalStorage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("currentUser", null);

        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }
}
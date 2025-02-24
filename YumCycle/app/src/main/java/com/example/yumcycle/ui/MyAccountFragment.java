// MyAccountFragment.java

package com.example.yumcycle.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.yumcycle.R;
import com.example.yumcycle.api.ApiClient;
import com.example.yumcycle.api.FavoritesService;
import com.example.yumcycle.api.RecipeService;
import com.example.yumcycle.models.Favorites;
import com.example.yumcycle.models.Recipe;
import com.example.yumcycle.models.User;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountFragment extends Fragment {

    private FavoritesService favoritesService;
    private RecipeService recipeService; // Add RecipeService

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener (v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_MyAccountFragment_to_ChooseProductFragment);
        });

        // Initialize Services
        favoritesService = ApiClient.getRetrofitInstance().create(FavoritesService.class);
        recipeService = ApiClient.getRetrofitInstance().create(RecipeService.class); // Initialize RecipeService

        // Retrieve user details from SharedPreferences
        User currentUser = getUserFromLocalStorage();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Display user details
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);

        usernameTextView.setText("Username: " + currentUser.getUsername());
        emailTextView.setText("Email: " + currentUser.getEmail());

        // Fetch and display favorite recipes
        LinearLayout favoritesContainer = view.findViewById(R.id.favoritesContainer);
        fetchFavorites(currentUser.getId(), favoritesContainer);
    }

    private void fetchFavorites(int userId, LinearLayout favoritesContainer) {
        favoritesService.getFavoritesByUserId(userId).enqueue(new Callback<List<Favorites>>() {
            @Override
            public void onResponse(Call<List<Favorites>> call, Response<List<Favorites>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Favorites> favoritesList = response.body();

                    if (favoritesList.isEmpty()) {
                        Toast.makeText(getContext(), "You have no favorite recipes.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (Favorites favorite : favoritesList) {
                        fetchRecipeDetails(favorite.getRecipeId(), favoritesContainer);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve favorites.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Favorites>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching favorites: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipeDetails(Long recipeId, LinearLayout favoritesContainer) {
        recipeService.getRecipeById(recipeId).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Recipe recipe = response.body();
                    addRecipeToUI(recipe, favoritesContainer);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve recipe details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching recipe: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRecipeToUI(Recipe recipe, LinearLayout favoritesContainer) {
        // Inflate the recipe_item layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View recipeItemView = inflater.inflate(R.layout.recipe_item, favoritesContainer, false);

        // Initialize the views
        TextView recipeTitleTextView = recipeItemView.findViewById(R.id.recipeTitleTextView);

        // Set the recipe details
        recipeTitleTextView.setText(recipe.getRecipeName());

        // Optionally, set click listeners for more interactions, such as viewing full details

        // Add the recipe item to the container
        favoritesContainer.addView(recipeItemView);
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
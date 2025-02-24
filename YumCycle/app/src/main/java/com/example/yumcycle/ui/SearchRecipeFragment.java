package com.example.yumcycle.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.yumcycle.api.InventoryService;
import com.example.yumcycle.api.OllamaService;
import com.example.yumcycle.models.Inventory;
import com.example.yumcycle.models.OllamaRequest;
import com.example.yumcycle.models.User;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchRecipeFragment extends Fragment {

    private LinearLayout ingredientContainer;
    private List<Inventory> selectedIngredients = new ArrayList<>();
    private Button generateRecipesButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientContainer = view.findViewById(R.id.ingredient_container);
        generateRecipesButton = view.findViewById(R.id.generate_recipes_button);
        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener (v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_SearchRecipeFragment_to_ChooseProductFragment);
        });

        User currentUser = getUserFromLocalStorage();
        if (currentUser != null) {
            fetchIngredientsByUserId(currentUser.getId());
        } else {
            Toast.makeText(getContext(), "User not logged in. Please log in to view your ingredients.", Toast.LENGTH_SHORT).show();
        }

        generateRecipesButton.setOnClickListener(v -> generateRecipesWithOllama());
    }

    private void fetchIngredientsByUserId(int userId) {
        InventoryService inventoryService = ApiClient.getRetrofitInstance().create(InventoryService.class);
        inventoryService.searchInventoryByUserId(userId).enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateIngredientList(response.body());
                } else {
                    Toast.makeText(getContext(), "No ingredients found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inventory>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load ingredients: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateIngredientList(List<Inventory> ingredientList) {
        // Sort the ingredient list by expiry date in ascending order
        Collections.sort(ingredientList, new Comparator<Inventory>() {
            @Override
            public int compare(Inventory ingredient1, Inventory ingredient2) {
                if (ingredient1.getExpiryDate() == null && ingredient2.getExpiryDate() == null) return 0;
                if (ingredient1.getExpiryDate() == null) return 1; // Nulls go to the end
                if (ingredient2.getExpiryDate() == null) return -1;
                return ingredient1.getExpiryDate().compareTo(ingredient2.getExpiryDate());
            }
        });

        // Clear the container
        ingredientContainer.removeAllViews();

        // Populate the sorted ingredients
        for (Inventory ingredient : ingredientList) {
            View ingredientItem = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_item, ingredientContainer, false);

            TextView ingredientName = ingredientItem.findViewById(R.id.ingredient_name);
            TextView ingredientQuantityUnit = ingredientItem.findViewById(R.id.ingredient_quantity_unit);
            Button selectButton = ingredientItem.findViewById(R.id.select_button);

            ingredientName.setText(ingredient.getIngredient().getIngredientName());
            String formattedQuantity = ingredient.getQuantity() + " " + ingredient.getUnit();
            ingredientQuantityUnit.setText(formattedQuantity);

            selectButton.setOnClickListener(v -> toggleSelection(ingredient, selectButton));

            ingredientContainer.addView(ingredientItem);
        }
    }

    private void toggleSelection(Inventory ingredient, Button selectButton) {
        if (selectedIngredients.contains(ingredient)) {
            selectedIngredients.remove(ingredient);
            selectButton.setText("Select");
            Toast.makeText(getContext(), "Ingredient deselected.", Toast.LENGTH_SHORT).show();
        } else {
            selectedIngredients.add(ingredient);
            selectButton.setText("Selected");
            Toast.makeText(getContext(), "Ingredient selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateRecipesWithOllama() {
        if (selectedIngredients.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one ingredient.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder promptBuilder = new StringBuilder("Generate a recipe and structure it like this: Title, Serving, Ingredients and clear, short Instructions on how to prepare it, leaving other comments out; containing these ingredients and others:\n");
        for (Inventory ingredient : selectedIngredients) {
            promptBuilder.append("- ")
                    .append(ingredient.getIngredient().getIngredientName())
                    .append(" (")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(")\n");
        }

//        String ollamaServerUrl = "http://192.168.0.229:11434/";
        String ollamaServerUrl = "http://172.20.10.2:11434/";




        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ollamaServerUrl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        OllamaService ollamaService = retrofit.create(OllamaService.class);

        OllamaRequest request = new OllamaRequest("llama3.2", promptBuilder.toString());
        ollamaService.generateRecipe(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String rawResponse = response.body();
                    Log.d("OllamaResponse", "Raw Response: " + rawResponse);

                    String combinedResponses = parseRawResponse(rawResponse);

                    if (combinedResponses != null) {
                        navigateToRecipeDisplay(combinedResponses);
                    } else {
                        Toast.makeText(getContext(), "Failed to parse recipe.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("OllamaResponse", "Error Response: " + errorBody);
                    } catch (Exception e) {
                        Log.e("OllamaResponse", "Error reading errorBody: " + e.getMessage());
                    }
                    Toast.makeText(getContext(), "Failed to generate recipes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("OllamaError", "Error generating recipes: " + t.getMessage());
                Toast.makeText(getContext(), "Error generating recipes: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToRecipeDisplay(String recipeData) {
        // Create a bundle to pass arguments
        Bundle bundle = new Bundle();
        bundle.putString("recipe_content", recipeData);
        bundle.putString("used_ingredients", new Gson().toJson(new ArrayList<>(selectedIngredients)));

        // Use NavController to navigate
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_SearchRecipeFragment_to_RecipeDisplayFragment, bundle);
    }

    private String parseRawResponse(String rawResponse) {
        StringBuilder parsedRecipe = new StringBuilder();
        String[] lines = rawResponse.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                JSONObject jsonObject = new JSONObject(line);
                if (jsonObject.has("response")) {
                    String response = jsonObject.getString("response");

                    // Append the response as-is (with any embedded newlines preserved)
                    parsedRecipe.append(response);
                }
            } catch (JSONException e) {
                Log.e("ParseError", "Error parsing line: " + line, e);
            }
        }

        // Log the final combined parsed recipe
        Log.d("ParseRawResponse", parsedRecipe.toString());

        // Return the parsed recipe as a string
        return parsedRecipe.toString();
    }

    private User getUserFromLocalStorage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("currentUser", null);

        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }
}
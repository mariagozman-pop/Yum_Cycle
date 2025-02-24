package com.example.yumcycle.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.yumcycle.models.Inventory;
import com.example.yumcycle.models.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryListFragment extends Fragment {

    private LinearLayout inventoryContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inventoryContainer = view.findViewById(R.id.inventory_table);
        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener (v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_InventoryListFragment_to_AddItemFragment);
        });

        User currentUser = getUserFromLocalStorage();
        if (currentUser != null) {
            fetchInventoryByUserId(currentUser.getId());
        } else {
            Toast.makeText(getContext(), "User not logged in. Please log in to view your inventory.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchInventoryByUserId(int userId) {
        InventoryService inventoryService = ApiClient.getRetrofitInstance().create(InventoryService.class);
        inventoryService.searchInventoryByUserId(userId).enqueue(new Callback<List<Inventory>>() {
            @Override
            public void onResponse(Call<List<Inventory>> call, Response<List<Inventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateInventoryList(response.body());
                } else {
                    Toast.makeText(getContext(), "No inventory items found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inventory>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load inventory: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateInventoryList(List<Inventory> inventoryList) {
        // Sort the inventory list by expiration date
        Collections.sort(inventoryList, new Comparator<Inventory>() {
            @Override
            public int compare(Inventory o1, Inventory o2) {
                return o1.getExpirationDate().compareTo(o2.getExpirationDate());
            }
        });

        inventoryContainer.removeAllViews(); // Clear existing views

        for (Inventory inventory : inventoryList) {
            View inventoryItem = LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, inventoryContainer, false);

            // Bind UI elements
            TextView ingredientName = inventoryItem.findViewById(R.id.ingredient_name);
            TextView ingredientQuantityUnit = inventoryItem.findViewById(R.id.ingredient_quantity_unit);
            TextView expirationDate = inventoryItem.findViewById(R.id.ingredient_expiration_date);
            Button deleteButton = inventoryItem.findViewById(R.id.delete_button);

            // Set data
            ingredientName.setText(inventory.getIngredient().getIngredientName());

            // Format quantity and unit
            String formattedQuantity = inventory.getQuantity() + " " + inventory.getUnit(); // e.g., "3 pcs / 250 g"
            ingredientQuantityUnit.setText(formattedQuantity);

            // Format expiration date (remove time part)
            String formattedDate = formatDate(inventory.getExpirationDate());
            expirationDate.setText(formattedDate);

            // Set delete action
            deleteButton.setOnClickListener(v -> deleteInventoryItem(inventory));

            // Add the item view to the container
            inventoryContainer.addView(inventoryItem);
        }
    }

    private String formatDate(String dateTime) {
        try {
            // Assuming the dateTime is in ISO format: "yyyy-MM-ddTHH:mm:ss"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(dateTime));
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A"; // Return a default value if parsing fails
        }
    }


    private void deleteInventoryItem(Inventory inventory) {
        InventoryService inventoryService = ApiClient.getRetrofitInstance().create(InventoryService.class);

        inventoryService.deleteInventoryItem(inventory.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item deleted.", Toast.LENGTH_SHORT).show();
                    fetchUpdatedInventoryList(); // Refresh the inventory list after deletion
                } else {
                    Toast.makeText(getContext(), "Failed to delete item.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpdatedInventoryList() {
        User currentUser = getUserFromLocalStorage();
        if (currentUser != null) {
            fetchInventoryByUserId(currentUser.getId());
        } else {
            Toast.makeText(getContext(), "User not logged in. Please log in to view your inventory.", Toast.LENGTH_SHORT).show();
        }
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
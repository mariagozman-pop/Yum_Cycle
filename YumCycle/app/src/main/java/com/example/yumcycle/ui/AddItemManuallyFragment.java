package com.example.yumcycle.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.yumcycle.R;
import com.example.yumcycle.api.ApiClient;
import com.example.yumcycle.api.IngredientService;
import com.example.yumcycle.api.InventoryService;
import com.example.yumcycle.api.UserService;
import com.example.yumcycle.models.Ingredient;
import com.example.yumcycle.models.Inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.yumcycle.models.User;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemManuallyFragment extends Fragment {

    private static final String TAG = "AddItemManuallyFragment";
    private EditText itemNameEditText;
    private EditText itemExpiryEditText;
    private EditText itemQuantityEditText;
    private Spinner itemUnitSpinner;
    private Button saveItemButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_manually, container, false);

        // Initialize views
        itemNameEditText = view.findViewById(R.id.item_name);
        itemExpiryEditText = view.findViewById(R.id.item_expiry);
        itemQuantityEditText = view.findViewById(R.id.item_quantity);
        itemUnitSpinner = view.findViewById(R.id.item_unit_spinner);
        saveItemButton = view.findViewById(R.id.save_item_button);
        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener (v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddItemManuallyFragment_to_AddItemFragment);
        });

        // Set up the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.unit_array, // Array from strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemUnitSpinner.setAdapter(adapter);

        // Handle selection events (optional)
        itemUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUnit = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Selected unit: " + selectedUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "No unit selected");
            }
        });

        // Set up the expiry date picker
        itemExpiryEditText.setOnClickListener(v -> showDatePickerDialog());

        // Set up save button logic
        saveItemButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString().trim();
            String itemExpiry = itemExpiryEditText.getText().toString().trim();
            String itemQuantity = itemQuantityEditText.getText().toString().trim();
            String itemUnit = itemUnitSpinner.getSelectedItem().toString();

            if (!itemName.isEmpty() && !itemExpiry.isEmpty() && !itemQuantity.isEmpty()) {
                processIngredient(itemName, itemExpiry, itemQuantity, itemUnit, 1L); // Replace 1L with actual user ID
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    // Set selected date in the expiry EditText
                    String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    itemExpiryEditText.setText(selectedDate);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void processIngredient(String name, String expiryDate, String quantity, String unit, Long userId) {
        IngredientService ingredientService = ApiClient.getRetrofitInstance().create(IngredientService.class);

        // Search for the ingredient first
        ingredientService.searchIngredientsByName(name).enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Ingredient already exists
                    Ingredient existingIngredient = response.body().get(0);
                    Log.d(TAG, "Existing ingredient found: " + existingIngredient.getId());
                    addInventory(existingIngredient.getId(), expiryDate, quantity, unit);
                } else {
                    // Ingredient doesn't exist, so create it
                    createIngredient(name, expiryDate, quantity, unit, userId);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to search for ingredient: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed while searching for ingredient", t);
            }
        });
    }

    private void createIngredient(String name, String expiryDate, String quantity, String unit, Long userId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName(name);

        try {
            // Convert the expiry date to ISO-8601 format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // User's input format
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US); // ISO-8601 format
            String formattedDate = isoFormat.format(inputFormat.parse(expiryDate));
            ingredient.setExpiryDate(formattedDate);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Failed to parse date", e);
            return;
        }

        IngredientService ingredientService = ApiClient.getRetrofitInstance().create(IngredientService.class);
        ingredientService.createIngredient(ingredient).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Ingredient created successfully! ID: " + response.body().getId(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Ingredient created: " + response.body());
                    addInventory(response.body().getId(), expiryDate, quantity, unit);
                } else {
                    Toast.makeText(getContext(), "Failed to create ingredient", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Ingredient> call, Throwable t) {
                Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed while creating ingredient", t);
            }
        });
    }

    private void addInventory(Long ingredientId, String expiryDate, String quantity, String unit) {
        InventoryService inventoryService = ApiClient.getRetrofitInstance().create(InventoryService.class);
        IngredientService ingredientService = ApiClient.getRetrofitInstance().create(IngredientService.class);

        // Fetch the current logged-in User from local storage
        User currentUser = getUserFromLocalStorage();

        if (currentUser != null) {
            // Log user information for debugging
            Log.d(TAG, "User retrieved from SharedPreferences: ID=" + currentUser.getId() + ", Username=" + currentUser.getUsername());

            // Fetch the Ingredient from the API
            ingredientService.getIngredientById(ingredientId).enqueue(new Callback<Ingredient>() {
                @Override
                public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Ingredient ingredient = response.body();
                        Log.d(TAG, "Fetched Ingredient: " + ingredient);

                        // Create and populate the Inventory object
                        Inventory inventory = new Inventory();
                        inventory.setIngredient(ingredient);
                        inventory.setUser(currentUser); // Use the locally stored user
                        inventory.setQuantity(quantity);
                        inventory.setUnit(unit);

                        try {
                            // Convert the expiryDate string to ISO-8601 format
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                            String formattedDate = isoFormat.format(inputFormat.parse(expiryDate));
                            inventory.setExpiryDate(formattedDate); // Set formatted expiryDate
                            Log.d(TAG, "Formatted Expiry Date: " + formattedDate);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to parse date", e);
                            return;
                        }

                        // Log inventory details before making the API call
                        Log.d(TAG, "Inventory details: IngredientID=" + inventory.getIngredient().getId() + ", UserID=" + inventory.getUser().getId()
                                + ", Quantity=" + inventory.getQuantity() + ", Unit=" + inventory.getUnit());

                        // Make the API call to create the Inventory
                        inventoryService.createInventory(inventory).enqueue(new Callback<Inventory>() {
                            @Override
                            public void onResponse(Call<Inventory> call, Response<Inventory> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(getContext(), "Inventory added successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Inventory added: " + response.body());
                                } else {
                                    Toast.makeText(getContext(), "Failed to add inventory", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error response: " + response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<Inventory> call, Throwable t) {
                                Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "API call failed while adding inventory", t);
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Ingredient not found", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error fetching Ingredient: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Ingredient> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to fetch Ingredient: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed while fetching Ingredient", t);
                }
            });
        } else {
            Toast.makeText(getContext(), "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User is null in SharedPreferences");
        }
    }


    private void fetchCurrentUser(UserCallback callback) {
        UserService userService = ApiClient.getRetrofitInstance().create(UserService.class);
        userService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onUserFetched(response.body());
                } else {
                    callback.onUserFetched(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onUserFetched(null);
            }
        });
    }

    interface UserCallback {
        void onUserFetched(User user);
    }

    private User getUserFromLocalStorage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("currentUser", null);

        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class); // Parse JSON back into User object
        }
        return null;
    }
}
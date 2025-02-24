package com.example.yumcycle.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yumcycle.R;

public class AddItemFragment extends Fragment {

    private EditText searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchBar = view.findViewById(R.id.search_bar);
        View magnifierIcon = view.findViewById(R.id.menu_button);
        View barcodeScanningButton = view.findViewById(R.id.barcode_scanning_button);
        View manuallyAddingButton = view.findViewById(R.id.manual_adding_button);
        View logoutButton = view.findViewById(R.id.logout_button);
        View backButton = view.findViewById(R.id.custom_back_button);

        backButton.setOnClickListener (v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddItemFragment_to_ChooseProductFragment);
        });

        // Set up navigation to Barcode Scanning Fragment
        barcodeScanningButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddItemFragment_to_BarcodeScanningFragment);
        });

        // Set up navigation to Manually Adding Fragment
        manuallyAddingButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddItemFragment_to_AddItemManuallyFragment);
        });

        // Set up click listener for magnifier icon
        magnifierIcon.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            Bundle bundle = new Bundle();

            if (!TextUtils.isEmpty(query)) {
                // Pass the search query to the next fragment
                bundle.putString("query", query);
            }

            // Navigate to InventoryListFragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddItemFragment_to_InventoryListFragment, bundle);
        });

        // Optional: Set up logic for the logout button
        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here
        });
    }
}
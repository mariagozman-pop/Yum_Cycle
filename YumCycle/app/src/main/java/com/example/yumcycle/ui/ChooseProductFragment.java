package com.example.yumcycle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yumcycle.R;

public class ChooseProductFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind the LinearLayout buttons
        LinearLayout addItemsButton = view.findViewById(R.id.addItemsButton);
        LinearLayout searchRecipesButton = view.findViewById(R.id.searchRecipesButton);

        // Bind the my account button
        View myAccountButton = view.findViewById(R.id.logoutButton);

        // Set up the "Add Your Items Here" button
        addItemsButton.setOnClickListener(v -> {
            // Navigate to the Add Items Fragment
            Toast.makeText(getContext(), "Navigating to Add Items", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_ChooseProductFragment_to_AddItemFragment);
        });

        // Set up the "Search For Recipes Here" button
        searchRecipesButton.setOnClickListener(v -> {
            // Navigate to the Search Recipes Fragment
            Toast.makeText(getContext(), "Navigating to Search Recipes", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_ChooseProductFragment_to_SearchRecipesFragment);
        });

        // Set up the "My Account" button
        myAccountButton.setOnClickListener(v -> {
            // Navigate to the my account screen
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_ChooseProductFragment_to_MyAccountFragment);
        });
    }
}

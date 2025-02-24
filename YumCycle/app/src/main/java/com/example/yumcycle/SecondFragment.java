package com.example.yumcycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.yumcycle.api.ApiClient;
import com.example.yumcycle.api.UserService;
import com.example.yumcycle.models.LoginRequest;

import com.example.yumcycle.models.User;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI elements

        EditText usernameField = view.findViewById(R.id.username);
        EditText passwordField = view.findViewById(R.id.password);
        CheckBox notARobotCheck = view.findViewById(R.id.notARobot);
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView createAccountLink = view.findViewById(R.id.createAccount);

        // Handle Login Button Click
        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validate input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Username and Password are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!notARobotCheck.isChecked()) {
                Toast.makeText(getContext(), "Please confirm you're not a robot!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform login
            performLogin(username, password);
        });

        // Handle Create Account Link Click
        createAccountLink.setOnClickListener(v -> {
            // Navigate to CreateAccountFragment (RegisterFragment)
            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_CreateAccountFragment);
        });


    }

    private void performLogin(String username, String password) {
        // Create login request object
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Call the backend API
        UserService userService = ApiClient.getRetrofitInstance().create(UserService.class);
        Call<String> call = userService.loginUser(loginRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login successful
                    Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();

                    // Fetch user details after login
                    fetchAndSaveUser(username);

                    // Navigate to ChooseProductFragment
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_ChooseProductFragment);
                } else {
                    // Login failed
                    String errorMessage = "Login Failed: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle error
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveUserToLocalStorage(User user) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save user details as JSON
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("currentUser", userJson);
        editor.apply(); // Commit changes

        Log.d("Login", "User saved to SharedPreferences: " + userJson);
    }

    private void fetchAndSaveUser(String username) {
        UserService userService = ApiClient.getRetrofitInstance().create(UserService.class);
        Call<User> call = userService.getUserByUsername(username); // Ensure this endpoint exists in your backend

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body(); // Fetch the user object
                    saveUserToLocalStorage(loggedInUser); // Save user locally
                    Log.d("Login", "User fetched and saved: " + loggedInUser.getUsername());
                } else {
                    Log.e("Login", "Failed to fetch user details: " + response.code());
                    Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Login", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Error fetching user details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}


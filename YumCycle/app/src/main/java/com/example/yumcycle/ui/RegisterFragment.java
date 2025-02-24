// RegisterFragment.java
package com.example.yumcycle.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yumcycle.R;
import com.example.yumcycle.api.ApiClient;
import com.example.yumcycle.api.UserService;
import com.example.yumcycle.models.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind UI elements with correct IDs
        EditText emailField = view.findViewById(R.id.emailEditText);
        EditText usernameField = view.findViewById(R.id.usernameEditText);
        EditText passwordField = view.findViewById(R.id.passwordEditText);
        CheckBox termsCheckBox = view.findViewById(R.id.termsCheckBox);
        CheckBox privacyCheckBox = view.findViewById(R.id.privacyCheckBox);
        CheckBox notARobotCheckBox = view.findViewById(R.id.notARobotCheckBox);
        Button registerButton = view.findViewById(R.id.registerButton);

        // Initialize Retrofit UserService
        UserService userService = ApiClient.getRetrofitInstance().create(UserService.class);

        // Handle Register Button Click
        registerButton.setOnClickListener(v -> {

            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String email = emailField.getText().toString().trim();


            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!termsCheckBox.isChecked() || !privacyCheckBox.isChecked() || !notARobotCheckBox.isChecked()) {
                Toast.makeText(getContext(), "Please agree to all conditions.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate Email Format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.setError("Please enter a valid email");
                emailField.requestFocus();
                return;
            }

            // Validate Password Length
            if (password.length() < 6) {
                passwordField.setError("Password should be at least 6 characters");
                passwordField.requestFocus();
                return;
            }

            // Create User object
            User user = new User(username, email, password);

            // Disable Register Button to prevent multiple clicks
            registerButton.setEnabled(false);
            registerButton.setText("Registering...");

            // Make API Call
            Call<String> call = userService.registerUser(user);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    // Re-enable the button and reset text
                    registerButton.setEnabled(true);
                    registerButton.setText("Register");

                    if (response.isSuccessful()) {
                        // Handle success (200 OK)
                        Toast.makeText(getContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to ChooseProductFragment
                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.action_CreateAccountFragment_to_ChooseProductFragment);

                    } else {
                        // Handle error (e.g., 400 Bad Request)
                        Toast.makeText(getContext(), "Registration Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Handle network or other errors
                    registerButton.setEnabled(true);
                    registerButton.setText("Register");
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("RegisterFragment", "Registration Error", t);
                }
            });
        });
    }
}

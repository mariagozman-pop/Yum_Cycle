package com.example.leftover_rescue.CONTROLLER;

import com.example.leftover_rescue.MODEL.User;
import com.example.leftover_rescue.SERVICE.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user.
     *
     * @param user The user details.
     * @return A response indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String response = userService.registerUser(user);
        if (response.equals("User registered successfully!")) {
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body(response);
        } else {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "text/plain")
                    .body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean isValidUser = userService.validateLogin(user.getUsername(), user.getPassword());

        if (isValidUser) {
            User loggedInUser = userService.getUserByUsername(user.getUsername());
            return ResponseEntity.ok(loggedInUser); // Return full User object
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    /**
     * Get all users.
     *
     * @return A list of users.
     */
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get a user by ID.
     *
     * @param id The ID of the user.
     * @return A response containing the user details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Get a user by username.
     *
     * @param username The username of the user.
     * @return A response containing the user details.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username); // Get the User from the service

        // Check if user is null and return appropriate ResponseEntity
        if (user != null) {
            return ResponseEntity.ok(user); // Return 200 OK with the User object
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 NOT FOUND
        }
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return A response indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    /**
     * Delete a user by username.
     *
     * @param username The username of the user to delete.
     * @return A response indicating success or failure.
     */
    @DeleteMapping("/username/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        boolean isDeleted = userService.deleteUserByUsername(username);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}

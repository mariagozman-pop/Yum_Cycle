package com.example.leftover_rescue.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Entity representing a user in the system.
 */
@Entity
@Table(name = "user_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isPresent() {
        return isNotNullOrEmpty(username) &&
                isNotNullOrEmpty(email) &&
                isNotNullOrEmpty(password);
    }

    // Helper method to check if a string is neither null nor empty after trimming
    private boolean isNotNullOrEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public User get() {
        return this;
    }

    /**
     * Functional-style map method for transforming the User object.
     *
     * @param <R> The return type of the transformation.
     * @param mapper A function that transforms a User into type R.
     * @return The result of applying the function to this User.
     */
    public <R> R map(Function<User, R> mapper) {
        return mapper.apply(this);
    }
}

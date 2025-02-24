package com.example.leftover_rescue.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a recipe in the system.
 */
@Entity
@Table(name = "recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "recipe_name", nullable = false, columnDefinition = "TEXT")
    private String recipeName;

//    @ManyToMany
//    @JoinTable(
//            name = "favorites",  // The table tracking the many-to-many relationship
//            joinColumns = @JoinColumn(name = "recipe_id"),  // Foreign key to Recipe
//            inverseJoinColumns = @JoinColumn(name = "user_id")  // Foreign key to User
//    )
//    private List<User> users; // List of users who favorited this recipe
}

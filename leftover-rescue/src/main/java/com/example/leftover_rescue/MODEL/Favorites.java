package com.example.leftover_rescue.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a user's favorite recipes.
 */
@Entity
@Table(name = "favorites")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key



    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Relationship with the User

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;  // Relationship with the Recipe
}

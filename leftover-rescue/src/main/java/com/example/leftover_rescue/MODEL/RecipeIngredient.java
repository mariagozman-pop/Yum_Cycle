package com.example.leftover_rescue.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing an ingredient used in a recipe.
 */
@Entity
@Table(name = "recipe_ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;  // Relationship with the Recipe

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;  // Relationship with the Ingredient

    @Column(nullable = false)
    private String quantity;

    @Column
    private String unit;  // e.g. 'grams', 'cups'
}

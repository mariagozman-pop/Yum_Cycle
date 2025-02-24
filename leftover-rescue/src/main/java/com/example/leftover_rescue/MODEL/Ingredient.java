package com.example.leftover_rescue.MODEL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing an ingredient.
 */
@Entity
@Table(name = "ingredients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    @Column(nullable = false)
    private String ingredientName;

    @Column
    private LocalDateTime expiryDate;  // Expiry date for ingredients

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipeIngredients;  // Relationship with recipe_ingredients

    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore // Prevent serialization of the Inventory list in User
    private List<Inventory> inventories;  // Relationship with inventory
}

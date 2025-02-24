package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for RecipeIngredient entities.
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    // Custom queries can be added here if needed
}

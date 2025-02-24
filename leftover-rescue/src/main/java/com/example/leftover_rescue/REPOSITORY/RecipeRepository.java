package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Recipe entities.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Add custom queries here if needed
}

package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Ingredient entities.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // Custom query to find ingredients by name
    List<Ingredient> findByIngredientNameContainingIgnoreCase(String name);

}

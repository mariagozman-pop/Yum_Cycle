package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Favorites entities.
 */
@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
     /**
     * Finds all favorites associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of Favorites for the user.
     */
    List<Favorites> findByUserId(int userId);
}

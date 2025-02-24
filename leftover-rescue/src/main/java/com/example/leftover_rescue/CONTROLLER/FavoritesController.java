package com.example.leftover_rescue.CONTROLLER;

import com.example.leftover_rescue.MODEL.Favorites;
import com.example.leftover_rescue.SERVICE.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing favorites.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    @Autowired
    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping
    public ResponseEntity<List<Favorites>> getAllFavorites() {
        List<Favorites> favorites = favoritesService.getAllFavorites();
        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<Favorites> addFavorite(@RequestBody Favorites favorites) {
        Favorites savedFavorites = favoritesService.addFavorite(favorites);
        return ResponseEntity.ok(savedFavorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        favoritesService.removeFavorite(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all favorite entries for a specific user.
     *
     * @param userId The ID of the user whose favorites are to be retrieved.
     * @return A ResponseEntity containing the list of Favorites.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorites>> getFavoritesByUserId(@PathVariable int userId) {
        List<Favorites> favorites = favoritesService.getFavoritesByUserId(userId);
        if (favorites.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        }
        return ResponseEntity.ok(favorites); // HTTP 200 OK with body
    }
}

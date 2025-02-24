package com.example.leftover_rescue.SERVICE;

import com.example.leftover_rescue.MODEL.Favorites;
import com.example.leftover_rescue.REPOSITORY.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling favorites-related operations.
 */
@Service
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    @Autowired
    public FavoritesService(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    public List<Favorites> getAllFavorites() {
        return favoritesRepository.findAll();
    }

    public Favorites addFavorite(Favorites favorites) {
        return favoritesRepository.save(favorites);
    }

    public void removeFavorite(Long id) {
        favoritesRepository.deleteById(id);
    }

    /**
     * Retrieves all favorite entries for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Favorites associated with the user.
     */
    public List<Favorites> getFavoritesByUserId(int userId) {
        return favoritesRepository.findByUserId(userId);
    }

}

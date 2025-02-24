package com.example.yumcycle.api;

import com.example.yumcycle.models.Favorites;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Service interface for managing favorites.
 */
public interface FavoritesService {

    /**
     * Fetch all favorites for the user.
     *
     * @return A Call object encapsulating a List of Favorites objects.
     */
    @GET("/api/favorites")
    Call<List<Favorites>> getAllFavorites();

    /**
     * Add a new favorite for the user.
     *
     * @param favorites The Favorites object to add.
     * @return A Call object encapsulating the added Favorites object.
     */
    @POST("/api/favorites")
    Call<Favorites> addFavorite(@Body Favorites favorites);

    /**
     * Remove a favorite by its ID.
     *
     * @param id The ID of the favorite to remove.
     * @return A Call object encapsulating a Void response.
     */
    @DELETE("/api/favorites/{id}")
    Call<Void> removeFavorite(@Path("id") Long id);

    /**
     * Fetch all favorites for a specific user by userId.
     *
     * @param userId The ID of the user whose favorites are to be retrieved.
     * @return A Call object encapsulating a List of Favorites objects for the specified user.
     */
    @GET("/api/favorites/user/{userId}")
    Call<List<Favorites>> getFavoritesByUserId(@Path("userId") int userId);
}

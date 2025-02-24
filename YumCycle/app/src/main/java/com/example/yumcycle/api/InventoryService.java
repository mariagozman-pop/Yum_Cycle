package com.example.yumcycle.api;

import com.example.yumcycle.models.Inventory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InventoryService {

    // Get all inventory items
    @GET("/api/inventory")
    Call<List<Inventory>> getAllInventory();

    // Get an inventory item by ID
    @GET("/api/inventory/{id}")
    Call<Inventory> getInventoryById(@Path("id") Long id);

    // Add a new inventory item
    @POST("/api/inventory")
    Call<Inventory> createInventory(@Body Inventory inventory);

    // Update an existing inventory item
    @PUT("/api/inventory/{id}")
    Call<Inventory> updateInventory(@Path("id") Long id, @Body Inventory inventory);

    // Delete an inventory item
    @DELETE("/api/inventory/{id}")
    Call<Void> deleteInventory(@Path("id") Long id);

    // Search inventory by userId and optionally ingredientId
    @GET("/api/inventory/search")
    Call<List<Inventory>> searchInventory(
            @Query("userId") int userId,
            @Query("ingredientId") Long ingredientId
    );

    // Search inventory by userId only
    @GET("/api/inventory/search")
    Call<List<Inventory>> searchInventoryByUserId(
            @Query("userId") int userId
    );

    // Delete an inventory item by ID
    @DELETE("/api/inventory/{id}")
    Call<Void> deleteInventoryItem(@Path("id") Long id);

    // Update an inventory item by ID
    @PUT("/api/inventory/{id}")
    Call<Inventory> updateInventoryItem(@Path("id") Long id, @Body Inventory inventory);
}
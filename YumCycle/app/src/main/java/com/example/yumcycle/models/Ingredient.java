package com.example.yumcycle.models;

public class Ingredient {
    private Long id;
    private String ingredientName;
    private String expiryDate; // Change this to String

    // Getters and Setters
    public String getIngredientName() {
        return ingredientName;
    }

    public Long getId() {
        return id; // This will return the ID after retrieval from the database
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientName='" + ingredientName + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }

    public Long getIngredientId() {
        return id;
    }
}
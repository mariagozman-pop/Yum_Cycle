package com.example.yumcycle.models;

public class Favorites {
    private Long id;
    private User user;       // Changed from userId to User object
    private Recipe recipe;   // Changed from recipeId to Recipe object

    // Constructors
    public Favorites() {}

    public Favorites(Long id, User user, Recipe recipe) {
        this.id = id;
        this.user = user;
        this.recipe = recipe;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getRecipeId() {
        return recipe.getId();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
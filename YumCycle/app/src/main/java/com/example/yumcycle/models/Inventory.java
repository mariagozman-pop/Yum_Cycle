package com.example.yumcycle.models;

public class Inventory {
    private Long id;
    private Long userId; // Foreign key to the user
    private Long ingredientId; // Foreign key to the ingredient
    private String quantity; // Quantity of the ingredient
    private String unit; // Unit of the quantity (e.g., grams, cups)
    private String expiryDate; // Expiry date of the ingredient
    private Ingredient ingredient;
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", user_id=" + userId +
                ", ingredient_id=" + ingredientId +
                ", quantity='" + quantity + '\'' +
                ", unit='" + unit + '\'' +
                ", expiry_date=" + expiryDate +
                '}';
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public User getUser() {
        return user;
    }

    public String getExpirationDate() {
        return expiryDate;
    }
}

package com.example.yumcycle.models;

public class Recipe {

    private Long id;

    private String recipeName; // Renamed from 'name' to 'recipeName'

    public Recipe() {
    }

    public Recipe(String recipeName) {
        this.recipeName = recipeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipeName() { // Updated getter
        return recipeName;
    }

    public void setRecipeName(String recipeName) { // Updated setter
        this.recipeName = recipeName;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipeName='" + recipeName + '\'' +
                '}';
    }
}

package com.aleksandar.cookml.models;

public class Recipe {
    public String title;
    public String categories;
    public String servings;
    public String ingredients;
    public String description;

    public Recipe() {

    }

    public Recipe(String recipeTitle, String recipeCategory, String recipeServings, String recipeIngredients, String recipeDescription) {
        title = recipeTitle;
        categories = recipeCategory;
        servings = recipeServings;
        ingredients = recipeIngredients;
        description = recipeDescription;
    }

    public Recipe(String recipeTitle, String recipeDescription) {
        title = recipeTitle;
        description = recipeDescription;
    }

    public String toString() {
        return title + " " + " " + categories + " " + servings + " " + ingredients + " " + description;
    }
}

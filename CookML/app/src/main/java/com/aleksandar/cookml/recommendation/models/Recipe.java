package com.aleksandar.cookml.recommendation.models;

import com.aleksandar.cookml.recommendation.models.Ingredient;

public class Recipe {
    public String title;
    public String category;
    public float servings;
    public Ingredient[] ingredients;
    public String description;

    public Recipe(String recipeTitle, String recipeCategory, float recipeServings, Ingredient[] recipeIngredients, String recipeDescription) {
        title = recipeTitle;
        category = recipeCategory;
        servings = recipeServings;
        ingredients = recipeIngredients;
        description = recipeDescription;
    }
}

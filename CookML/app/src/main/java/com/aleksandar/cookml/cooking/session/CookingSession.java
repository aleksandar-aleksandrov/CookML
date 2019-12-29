package com.aleksandar.cookml.cooking.session;

import com.aleksandar.cookml.models.CheckableIngredient;
import com.aleksandar.cookml.models.Recipe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class CookingSession {
    private ArrayList<CheckableIngredient> ingredients;
    private ArrayList<Recipe> recommendedRecipes;

    @Inject
    public CookingSession() {
        ingredients = new ArrayList<CheckableIngredient>();
        recommendedRecipes = new ArrayList<Recipe>();
    }

    public ArrayList<Recipe> getRecommendedRecipes() {
        return recommendedRecipes;
    }

    public void addRecommendedRecipe(Recipe recipe) {
        recommendedRecipes.add(recipe);
    }

    public void addNewIngredient(CheckableIngredient ingredient) {
        ingredients.add(ingredient);
    }

    public ArrayList<CheckableIngredient> getIngredients() {
        return ingredients;
    }
}

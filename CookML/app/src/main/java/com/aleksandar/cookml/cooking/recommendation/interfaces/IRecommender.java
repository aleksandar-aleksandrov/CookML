package com.aleksandar.cookml.cooking.recommendation.interfaces;

import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;

import java.util.ArrayList;

public interface IRecommender {
    ArrayList<Recipe> recommend(ArrayList<Ingredient> ingredients);
}

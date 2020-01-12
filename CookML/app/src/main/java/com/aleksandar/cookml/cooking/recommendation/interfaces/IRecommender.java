package com.aleksandar.cookml.cooking.recommendation.interfaces;

import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public interface IRecommender {
    List<Recipe> recommend(List<Ingredient> ingredients);
}

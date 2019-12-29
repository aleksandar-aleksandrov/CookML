package com.aleksandar.cookml.cooking.recommendation;

import com.aleksandar.cookml.cooking.recommendation.interfaces.IRecommender;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;



public class RecipeRecommender implements IRecommender {
    @Override
    public ArrayList<Recipe> recommend(ArrayList<Ingredient> ingredients) {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add(new Recipe("Mashed Potatoes", "sadjfjasjdfajsdfjasfasdfasdf"));
        recipes.add(new Recipe("Cooked Tomatoes", "asdfasdfafffffffffffffffffffffffffff"));

        return recipes;
    }
}

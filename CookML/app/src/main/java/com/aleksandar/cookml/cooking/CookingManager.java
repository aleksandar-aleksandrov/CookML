package com.aleksandar.cookml.cooking;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.cooking.interfaces.ICookingManager;
import com.aleksandar.cookml.cooking.recommendation.RecipeRecommender;
import com.aleksandar.cookml.models.CheckableIngredient;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;
import com.aleksandar.cookml.cooking.recognition.IngredientsRecognizer;
import com.aleksandar.cookml.cooking.session.CookingSession;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CookingManager implements ICookingManager {

    private CookingSession session;
    private IngredientsRecognizer recognizer;
    private RecipeRecommender recommender;

    @Inject
    public CookingManager() {
        session = new CookingSession();
        recognizer = new IngredientsRecognizer();
        recommender = new RecipeRecommender();
    }

    @Override
    public ArrayList<CheckableIngredient> recognize(AppCompatActivity activity, Bitmap bitmap) {
        ArrayList<Ingredient> recognizedIngredients = null;
        try {
            recognizedIngredients = recognizer.recognize(activity, bitmap);
            session.clearIngredients();
            for(Ingredient e : recognizedIngredients) {
                session.addNewIngredient(new CheckableIngredient(e.name, true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return session.getIngredients();
    }

    @Override
    public ArrayList<CheckableIngredient> getIngredients() {
        return session.getIngredients();
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        session.addNewIngredient(new CheckableIngredient(ingredient.name, true));
    }

    @Override
    public ArrayList<Recipe> recommend(AppCompatActivity activity) {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        for(CheckableIngredient ingredient : getIngredients()) {
            if(ingredient.isChecked()) {
                ingredients.add(new Ingredient(ingredient.name));
            }
        }

        session.clearRecipes();
        List<Recipe> recipes = recommender.recommend(activity.getAssets(), ingredients);
        for(Recipe recipe : recipes) {
            session.addRecommendedRecipe(recipe);
        }

        return (ArrayList<Recipe>) recipes;
    }

    @Override
    public ArrayList<Recipe> getRecipes() {
        return session.getRecommendedRecipes();
    }

    public void addCheckableIngredient(CheckableIngredient ingredient) {
        session.addNewIngredient(ingredient);
    }

    public boolean atLeastOneCheckedIngredient() {
        for(CheckableIngredient ingredient : session.getIngredients()) {
            if(ingredient.isChecked()) {
                return true;
            }
        }

        return false;
    }
}

package com.aleksandar.cookml.cooking.interfaces;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.models.CheckableIngredient;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.models.Recipe;

import java.util.ArrayList;

public interface ICookingManager {
    ArrayList<CheckableIngredient> recognize(AppCompatActivity activity, Bitmap bitmap);

    ArrayList<CheckableIngredient> getIngredients();
    void addIngredient(Ingredient ingredient);

    ArrayList<Recipe> recommend(AppCompatActivity activity);

    ArrayList<Recipe> getRecipes();
}

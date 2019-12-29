package com.aleksandar.cookml;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aleksandar.cookml.cooking.CookingManager;
import com.aleksandar.cookml.cooking.CookingManagerComponent;
import com.aleksandar.cookml.models.CheckableIngredient;
import com.aleksandar.cookml.models.Recipe;

import javax.inject.Inject;

public class RecipeRecomendationActivity extends AppCompatActivity {
    @Inject
    CookingManager cookingManager;

    private Button nextButton;
    private Button prevButton;
    private TextView recipeDescriptionView;
    private TextView recipeNameView;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_recomendation);

        CookingManagerComponent component = ((CookMLApp) getApplication()).getCookingManagerComponent();
        component.inject(this);

        recipeDescriptionView = (TextView) findViewById(R.id.recipeDescription);
        recipeNameView = (TextView) findViewById(R.id.recipeName);
        nextButton = (Button) findViewById(R.id.next);
        prevButton = (Button) findViewById(R.id.prev);

        updateRecipeView(cookingManager.getRecipes().get(0));
    }

    public void updateRecipeView(Recipe recipe) {
        recipeDescriptionView.setText(recipe.description);
        recipeNameView.setText(recipe.title);
    }

    public void onNext(View view) {
        currentIndex++;
        System.out.println(currentIndex);
        System.out.println(cookingManager.getRecipes().size());
        if(currentIndex >= cookingManager.getRecipes().size() - 1) {
            currentIndex = cookingManager.getRecipes().size() - 1;
            //nextButton.setClickable(false);
        }

        //prevButton.setClickable(true);

        updateRecipeView(cookingManager.getRecipes().get(currentIndex));
    }

    public void onPrev(View view) {
        currentIndex--;

        if(currentIndex <= 0) {
            currentIndex = 0;
            //prevButton.setClickable(false);
        }

        //nextButton.setClickable(true);

        updateRecipeView(cookingManager.getRecipes().get(currentIndex));
    }
}

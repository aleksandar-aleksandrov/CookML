package com.aleksandar.cookml.cooking;

import com.aleksandar.cookml.IngredientRecognitionActivity;
import com.aleksandar.cookml.IngredientSelectionActivity;
import com.aleksandar.cookml.RecipeRecomendationActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface CookingManagerComponent {
    CookingManager getCookingManager();

    void inject(IngredientSelectionActivity activity);
    void inject(IngredientRecognitionActivity activity);
    void inject(RecipeRecomendationActivity activity);
}

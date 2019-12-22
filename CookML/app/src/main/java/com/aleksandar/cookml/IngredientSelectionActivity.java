package com.aleksandar.cookml;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aleksandar.cookml.recommendation.classification.KNNClassifier;
import com.aleksandar.cookml.recommendation.models.Ingredient;
import com.aleksandar.cookml.recommendation.models.Prediction;
import com.aleksandar.cookml.recommendation.models.Recipe;
import com.aleksandar.cookml.recommendation.preprocessing.DataPreprocessor;

import java.util.ArrayList;

public class IngredientSelectionActivity extends AppCompatActivity {
    public ArrayList<String> detectedIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectedIngredients.add("c");
        detectedIngredients.add("tomato");
        detectedIngredients.add("cucumber");

        setContentView(R.layout.activity_ingredient_selection);

        LinearLayout myRoot = (LinearLayout) findViewById(R.id.detectedLayout);

        for(String i : detectedIngredients) {
            CheckBox ch = new CheckBox(this);
            ch.setText(i);
            ch.setChecked(true);
            myRoot.addView(ch);
        }

        System.out.println(myRoot);

    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        System.out.println(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    public void addIngredient(View view) {
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.detectedLayout);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        CheckBox ch = new CheckBox(this);
        System.out.println(view);
        ch.setText(textView.getText());
        ch.setChecked(true);
        myRoot.addView(ch);
    }

    public void startSpinner(View view) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        View background = (View) findViewById(R.id.background_dim);
        Button cookButton = (Button) findViewById(R.id.button2);
        Button addButton = (Button) findViewById(R.id.button);

        progressBar.setVisibility(View.VISIBLE);
        background.setVisibility(View.VISIBLE);
        cookButton.setClickable(false);
        addButton.setClickable(false);

        Runnable r = new Runnable() {
            @Override
            public void run(){
                move();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 2000); // <-- the "1000" is the delay time in miliseconds.
    }

    public void move() {
        // Load the data
        DataPreprocessor preprocessor = new DataPreprocessor();
        ArrayList<Recipe> recipes = preprocessor.load();
        ArrayList<double[]> vectors = new ArrayList<>();
        for(Recipe recipe : recipes) {
            vectors.add(preprocessor.toVector(recipe.ingredients));
        }

        // Create the classifier
        KNNClassifier classifier = new KNNClassifier(3);

        // Fit the classifier
        classifier.fit((double[][]) vectors.toArray(), recipes);

        // com.aleksandar.cookml.recommendation.models.Prediction
        Ingredient[] ingredients = (Ingredient[]) new ArrayList<Ingredient>().toArray();
        double[] vectorizedIngredients = preprocessor.toVector(ingredients);
        ArrayList<Prediction> recommendedRecipes = classifier.predict(vectorizedIngredients);
        Intent myIntent = new Intent(this, RecipeRecomendationActivity.class);
        startActivity(myIntent);
    }
}

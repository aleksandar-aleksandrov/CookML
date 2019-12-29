package com.aleksandar.cookml;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.cooking.CookingManager;
import com.aleksandar.cookml.cooking.CookingManagerComponent;
import com.aleksandar.cookml.models.CheckableIngredient;

import javax.inject.Inject;
import java.util.ArrayList;

public class IngredientSelectionActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private View background;
    private Button cookButton;
    private Button addButton;

    @Inject
    CookingManager cookingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_selection);

        CookingManagerComponent component = ((CookMLApp) getApplication()).getCookingManagerComponent();
        component.inject(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        background = (View) findViewById(R.id.background_dim);
        cookButton = (Button) findViewById(R.id.button2);
        addButton = (Button) findViewById(R.id.button);

        addCheckboxes(cookingManager.getIngredients());
    }

    private void addCheckBox(final CheckableIngredient ingredient) {
        LinearLayout root = (LinearLayout) findViewById(R.id.detectedLayout);
        addCheckbox(ingredient, root);
    }

    private void addCheckbox(final CheckableIngredient ingredient, LinearLayout root) {
        CheckBox ch = new CheckBox(this);
        ch.setText(ingredient.name);
        ch.setChecked(ingredient.isChecked());
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ingredient.toggle();
            }
        });
        root.addView(ch);
    }

    private void addCheckboxes(final ArrayList<CheckableIngredient> ingredients) {
        LinearLayout root = (LinearLayout) findViewById(R.id.detectedLayout);

        for(final CheckableIngredient ingredient : ingredients) {
            addCheckbox(ingredient, root);
        }
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
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final CheckableIngredient ingredient = new CheckableIngredient(textView.getText().toString(), true);
        addCheckBox(ingredient);
        cookingManager.addCheckableIngredient(ingredient);
    }

    public void startSpinner(View view) {
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
        progressBar.setVisibility(View.INVISIBLE);
        background.setVisibility(View.INVISIBLE);
        cookButton.setClickable(true);
        addButton.setClickable(true);

        cookingManager.recommend();
        Intent myIntent = new Intent(this, RecipeRecomendationActivity.class);
        startActivity(myIntent);
    }
}

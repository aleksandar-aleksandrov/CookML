package com.aleksandar.cookml.cooking.recommendation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import com.aleksandar.cookml.models.Recipe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecipeJSONParser
{
    public static String loadJSON(AssetManager assetManager, String fileName) {
        String json = null;
        try {
            InputStream is = assetManager.open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static List<Recipe> parse(AssetManager assetManager, String fileName)
    {
        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            JSONArray recipesList = new JSONArray(loadJSON(assetManager, fileName));

            //Iterate over employee array
            for(int i = 0; i < recipesList.length(); i++) {
                recipes.add(parseRecipe((JSONObject) recipesList.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    private static Recipe parseRecipe(JSONObject recipeObject) throws JSONException {
        Recipe r = new Recipe();

        r.title = (String) recipeObject.get("Title");
        r.categories = (String) recipeObject.get("Categories");
        r.servings = (String) recipeObject.get("Servings");
        r.description = (String) recipeObject.get("Description");
        r.ingredients = (String) recipeObject.get("Ingredients");

        return r;
    }

}

package com.aleksandar.cookml.recommendation.preprocessing;

import com.aleksandar.cookml.recommendation.models.Ingredient;
import com.aleksandar.cookml.recommendation.models.Recipe;

import java.util.ArrayList;

public class DataPreprocessor {
    public ArrayList<Recipe> load() {
        return new ArrayList<Recipe>();
    }

    public double[] toVector(Ingredient[] ingredients) {
        return new double[1];
    }

    public ArrayList<double[]> toVector(Ingredient[][] ingredients) {
        ArrayList<double[]> vectors = new ArrayList<double[]>();

        for (Ingredient[] ingredient : ingredients) {
            vectors.add(this.toVector(ingredient));
        }

        return vectors;
    }
}

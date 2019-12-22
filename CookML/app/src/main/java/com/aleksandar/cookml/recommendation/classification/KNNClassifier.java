package com.aleksandar.cookml.recommendation.classification;

import com.aleksandar.cookml.recommendation.models.Prediction;
import com.aleksandar.cookml.recommendation.models.Recipe;

import java.util.ArrayList;
import java.util.Collections;


public class KNNClassifier {
    private int k;

    private double[][] X;
    private ArrayList<Recipe> Y;

    public KNNClassifier(int k) {
        this.k = k;
    }

    public void fit(double[][] samples, ArrayList<Recipe> targets) {
        this.X = samples;
        this.Y = targets;
    }

    public ArrayList<Prediction> predict(double[] sample) {
        ArrayList<Prediction> topPredictions = new ArrayList<Prediction>();

        for(int i = 0; i < this.X.length; i++) {
            Prediction newPrediction = new Prediction(this.calculateDistance(this.X[i], sample), this.Y.get(i));
            topPredictions.add(newPrediction);
            Collections.sort(topPredictions);

            if(i % this.k * 2 == 0 && i != 0) {
                topPredictions = new ArrayList<Prediction>(topPredictions.subList(0, this.k-1));
            }
        }

        return topPredictions;
    }

    private double calculateDistance(double[] a, double[] b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for(int i = 0; i < a.length; i++) {
            dotProduct += (a[i] * b[i]);
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }

        return dotProduct / Math.sqrt(normA) * Math.sqrt(normB);
    }
}

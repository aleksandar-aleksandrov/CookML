package com.aleksandar.cookml.recommendation.models;

public class Prediction implements Comparable<Prediction> {
    private double score;
    private Recipe recipe;

    public Prediction(double score, Recipe recipe) {
        this.score = score;
        this.recipe = recipe;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(Prediction o) {
        if(this.score == o.getScore()) {
            return 0;
        } else if(this.score > o.getScore()) {
            return 1;
        }

        return -1;
    }
}
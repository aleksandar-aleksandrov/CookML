package com.aleksandar.cookml.models;

public class CheckableIngredient extends Ingredient {
    private boolean checked;

    public CheckableIngredient(String ingredientName, boolean checked) {
        super(ingredientName);

        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void uncheck() {
        checked = false;
    }

    public void check() {
        checked = true;
    }

    public void toggle() {
        checked = !checked;
    }
}

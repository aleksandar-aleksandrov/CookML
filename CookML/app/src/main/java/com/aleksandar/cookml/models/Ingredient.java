package com.aleksandar.cookml.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String name;

    public Ingredient(String ingredientName) {
        name = ingredientName;
    }
}

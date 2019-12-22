package com.aleksandar.cookml.models;

public class Recipe {
    protected String Name;
    protected String Description;

    public Recipe(String name, String description) {
        Name = name;
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public String getName() {
        return Name;
    }
}

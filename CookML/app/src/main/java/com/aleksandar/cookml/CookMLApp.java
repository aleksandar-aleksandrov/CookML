package com.aleksandar.cookml;

import com.aleksandar.cookml.cooking.CookingManagerComponent;
import com.aleksandar.cookml.cooking.DaggerCookingManagerComponent;

public class CookMLApp extends android.app.Application {
    private CookingManagerComponent cookingManagerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        cookingManagerComponent = DaggerCookingManagerComponent.create();
    }

    public CookingManagerComponent getCookingManagerComponent() {
        return cookingManagerComponent;
    }
}

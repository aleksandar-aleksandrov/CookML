package com.aleksandar.cookml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startCooking(View view)
    {
        Intent myIntent = new Intent(this, IngredientRecognitionActivity.class);
        startActivity(myIntent);
    }

}

package com.aleksandar.cookml;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        System.out.println("asdfasdfasd");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void startCooking(View view)
    {
        System.out.println("hahah");
        Intent myIntent = new Intent(this, IngredientSelectionActivity.class);
        startActivity(myIntent);

    }

}

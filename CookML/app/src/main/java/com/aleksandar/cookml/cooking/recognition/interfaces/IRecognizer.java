package com.aleksandar.cookml.cooking.recognition.interfaces;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.models.Ingredient;

import java.io.IOException;
import java.util.ArrayList;

public interface IRecognizer {
    ArrayList<Ingredient> recognize(AppCompatActivity activity, Bitmap bitmap) throws IOException;
}

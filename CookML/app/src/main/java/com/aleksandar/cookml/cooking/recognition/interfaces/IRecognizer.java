package com.aleksandar.cookml.cooking.recognition.interfaces;

import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.models.Ingredient;

import java.io.IOException;
import java.util.ArrayList;

public interface IRecognizer {
    ArrayList<Ingredient> recognize(AppCompatActivity activity) throws IOException;
}

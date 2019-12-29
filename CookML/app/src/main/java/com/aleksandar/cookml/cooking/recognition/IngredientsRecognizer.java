package com.aleksandar.cookml.cooking.recognition;

import android.app.Activity;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.aleksandar.cookml.cooking.recognition.interfaces.IRecognizer;
import com.aleksandar.cookml.models.Ingredient;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;


public class IngredientsRecognizer implements IRecognizer {
    @Override
    public ArrayList<Ingredient> recognize(AppCompatActivity activity) throws IOException {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        ingredients.add(new Ingredient("gosho"));
        ingredients.add(new Ingredient("pesho"));

        return ingredients;
    }
}

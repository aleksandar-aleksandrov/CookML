package com.aleksandar.cookml.cooking.recognition;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.cooking.recognition.interfaces.IRecognizer;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.cooking.recognition.tflite.Classifier;
import com.aleksandar.cookml.cooking.recognition.tflite.YoloClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class IngredientsRecognizer implements IRecognizer {
    private YoloClassifier classifier;

    private static final String MODEL_FILE = "converted_model2.tflite";
    private static final String LABELS_FILE = "file:///android_asset/classes.txt";
    private static final int INPUT_SIZE = 224;

    @Override
    public ArrayList<Ingredient> recognize(AppCompatActivity activity, Bitmap bitmap) throws IOException {
        classifier = new YoloClassifier(activity.getAssets(), MODEL_FILE, LABELS_FILE, INPUT_SIZE);

        List<Classifier.Recogniton> recognitions = classifier.recognizeImage(bitmap);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        ingredients.add(new Ingredient(recognitions.get(0).className));
        ingredients.add(new Ingredient(recognitions.get(1).className));

        return ingredients;
    }
}

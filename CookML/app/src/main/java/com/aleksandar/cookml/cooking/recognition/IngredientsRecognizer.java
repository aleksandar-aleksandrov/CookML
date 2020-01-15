package com.aleksandar.cookml.cooking.recognition;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import com.aleksandar.cookml.cooking.recognition.interfaces.IRecognizer;
import com.aleksandar.cookml.models.Ingredient;
import com.aleksandar.cookml.cooking.recognition.tflite.Classifier;
import com.aleksandar.cookml.cooking.recognition.tflite.YoloClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class IngredientsRecognizer implements IRecognizer {
    private YoloClassifier classifier = null;

    private static final String MODEL_FILE = "converted_model.tflite";
    private static final String LABELS_FILE = "file:///android_asset/classes.txt";
    private static final int INPUT_SIZE = 224;
    private static final int N = 10;

    @Override
    public ArrayList<Ingredient> recognize(AppCompatActivity activity, Bitmap bitmap) throws IOException {
        if(classifier == null) {
            classifier = new YoloClassifier(activity.getAssets(), MODEL_FILE, LABELS_FILE, INPUT_SIZE);
        }

        List<Classifier.Recogniton> recognitions = classifier.recognizeImage(bitmap);
        Collections.sort(recognitions, Collections.reverseOrder(new Comparator<Classifier.Recogniton>() {
            @Override
            public int compare(Classifier.Recogniton o1, Classifier.Recogniton o2) {
                if( o1.classPercentage < o2.classPercentage) {
                    return -1;
                } else if (o2.classPercentage > o2.classPercentage) {
                    return 1;
                }
                return 0;
            }
        }));
        recognitions = recognitions.subList(0, 10);
        Collections.sort(recognitions, Collections.reverseOrder(new Comparator<Classifier.Recogniton>() {
            @Override
            public int compare(Classifier.Recogniton o1, Classifier.Recogniton o2) {
                if( o1.classPercentage * o1.confidence < o2.classPercentage * o2.confidence) {
                    return -1;
                } else if (o2.classPercentage * o1.confidence > o2.classPercentage * o2.confidence) {
                    return 1;
                }
                return 0;
            }
        }));
        System.out.println(recognitions.size());
        System.out.println("###############################################");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        for(int i = 0; i < N; i++) {
            Classifier.Recogniton recognition = recognitions.get(i);
            System.out.println(recognition.className);
            System.out.println(recognition.classPercentage);
            System.out.println(recognition.confidence);
            ingredients.add(new Ingredient(recognition.className));
        }

        return ingredients;
    }
}

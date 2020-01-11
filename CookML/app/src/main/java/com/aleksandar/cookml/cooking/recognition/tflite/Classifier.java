package com.aleksandar.cookml.cooking.recognition.tflite;

import android.graphics.Bitmap;
import java.util.List;

/** Generic interface for interacting with different recognition engines. */
public interface Classifier {
    List<Recogniton> recognizeImage(Bitmap bitmap);

    public class Recogniton {
        public float x;
        public float y;
        public float w;
        public float h;
        public float confidence;
        public String className;
        public float classPercentage;

        public Recogniton(float x, float y, float w, float h, float confidence, String className, float classPercentage) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.confidence = confidence;
            this.className = className;
            this.classPercentage = classPercentage;
        }
    }
}

package com.aleksandar.cookml;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.aleksandar.cookml.cooking.CookingManager;
import com.aleksandar.cookml.cooking.CookingManagerComponent;
import com.aleksandar.cookml.cooking.session.CookingSession;
import com.aleksandar.cookml.models.CheckableIngredient;
import com.aleksandar.cookml.tflite.Classifier;
import com.aleksandar.cookml.tflite.ClassifierFloatMobileNet;
import com.aleksandar.cookml.tflite.ClassifierQuantizedMobileNet;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class IngredientRecognitionActivity extends AppCompatActivity {
    @Inject
    CookingManager cookingManager;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detection);

        CookingManagerComponent component = ((CookMLApp) getApplication()).getCookingManagerComponent();
        component.inject(this);

        this.imageView =  this.findViewById(R.id.generatedPhoto);
    }

    public void onClick(View v) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    public void onTriggerDetection(View v) throws IOException {
        cookingManager.recognize(this);

        Intent nextIntent = new Intent(this, IngredientSelectionActivity.class);
        startActivity(nextIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
              Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photo = Bitmap.createScaledBitmap(photo, 224, 224, false);
            try {
                ClassifierQuantizedMobileNet clf = new ClassifierQuantizedMobileNet(this, Classifier.Device.CPU, 1);
                List<Classifier.Recognition> recog = clf.recognizeImage(photo, 0);
                for(Classifier.Recognition r : recog) {
                    System.out.println(r.getTitle());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(photo);
        }
    }
}
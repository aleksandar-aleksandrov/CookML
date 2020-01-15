package com.aleksandar.cookml.cooking.recognition.tflite;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Trace;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import org.tensorflow.lite.Interpreter;

public class YoloClassifier implements Classifier {
  private static final float IMAGE_MEAN = 0.0f;
  private static final float IMAGE_STD = 255.0f;
  private static final int S = 7;
  private static final int B = 1;
  private static final int C = 25;

  private int inputSize;
  private Vector<String> labels = new Vector<String>();
  private int[] intValues;
  private ByteBuffer imgData;

  private Interpreter tfLite;

  public YoloClassifier(
          final AssetManager assetManager,
          final String modelFilename,
          final String labelFilename,
          final int inputSize)
          throws IOException {
      this.loadLabels(assetManager, labelFilename);
      this.inputSize = inputSize;

      try {
          this.tfLite = new Interpreter(loadModelFile(assetManager, modelFilename));
      } catch (Exception e) {
          throw new RuntimeException(e);
      }

      this.imgData = ByteBuffer.allocateDirect(this.inputSize * this.inputSize * 3 * 4);
      this.imgData.order(ByteOrder.nativeOrder());
      this.intValues = new int[this.inputSize * this.inputSize];

      this.tfLite.setNumThreads(4);
  }

  public float sigmoid(float x) {
      return (float) (1/( 1 + Math.pow(Math.E, (-1*x))));
  }

  private void loadLabels(AssetManager assetManager, String labelFilename) throws IOException {
      InputStream labelsInput = null;
      String actualFilename = labelFilename.split("file:///android_asset/")[1];
      labelsInput = assetManager.open(actualFilename);

      BufferedReader br = null;
      br = new BufferedReader(new InputStreamReader(labelsInput));

      String line;
      while ((line = br.readLine()) != null) {
          this.labels.add(line);
      }

      br.close();
  }

  private MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
      throws IOException {
      AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
      FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
      FileChannel fileChannel = inputStream.getChannel();
      long startOffset = fileDescriptor.getStartOffset();
      long declaredLength = fileDescriptor.getDeclaredLength();
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
  }

  @Override
  public List<Recogniton> recognizeImage(final Bitmap bitmap) {
      bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

      imgData.rewind();
      for (int i = 0; i < inputSize; ++i) {
        for (int j = 0; j < inputSize; ++j) {
          int pixelValue = intValues[i * inputSize + j];

          imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
          imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
          imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        }
      }


      float[][][][][] output = new float[1][S][S][B][C + 5];
      Map<Integer, Object> outputMap = new HashMap<>();
      outputMap.put(0, output);
      Trace.endSection();

      tfLite.runForMultipleInputsOutputs(new Object[]{imgData}, outputMap);

      return extractRecognitions(output);
  }

  private List<Recogniton> extractRecognitions(float [][][][][] output) {
    final ArrayList<Recogniton> recognitions = new ArrayList<>();

    for(int row = 0; row < S; row++) {
      for(int col = 0; col < S; col++) {
        for(int bb = 0; bb < B; bb++) {
          float[] bounding_box = output[0][row][col][bb];

          float x = this.sigmoid(bounding_box[0]);
          float y = this.sigmoid(bounding_box[1]);
          float w = this.sigmoid(bounding_box[2]);
          float h = this.sigmoid(bounding_box[3]);
          float confidence = this.sigmoid(bounding_box[4]);

          float max_value = 0;
          int max_index = 0;
          float sf_sum = 0;

          for(int c = 5; c < C + 5; c++) {
              float class_probability = (float) Math.pow(Math.E, bounding_box[c]);
              if(class_probability >= max_value) {
                  max_value = class_probability;
                  max_index = c - 5;
              }
            sf_sum += class_probability;
          }

          Recogniton recognition = new Recogniton(x, y, w, h, confidence, this.labels.elementAt(max_index), max_value / sf_sum);
          recognitions.add(recognition);
        }
      }
    }

    return recognitions;
  }
}

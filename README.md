# CookML

![logo](assets/logo_big.png)

## Description

CookML is an Android mobile application which implements a recipe recommedation system with an embedded visual recognition of available food ingredients.

## Folders
- `apk` - A reusable apk version of the application
- `assets` - The different sizes of the logo of the application
- `metrics` - Some recorded metrics (execution time of the inference methods in ms)
- `models` - A trained YOLO model in the `.pb` format and `.tflite` format. In the `jupyter-notebook` subfolder, the notebook used for training the model can be found. In the `loss` subfolder, the loss during the training is saved.
- `CookML` - The actual Android application

## How-tos

### How to run the application?
1. The first way to run the application would be to load the `CookML` project in InteliJ IDEA IDE or a similar IDE software and build the project and run it either directly on a mobile device by connecting your phone via USB, or on a virtual emulated device on your PC (this one is computationally more taxing to your PC/Laptop).
2. Second possibility is to install the delivered apk in the `apk/release` folder directly on your mobile device.
3. The third and easiest way to run the application is to go to the `Google Play Store` and download it directly to your mobile device. Your device should be running a version of Android of 7.1 or higher (API 25 or higher). The application can be found at https://play.google.com/store/apps/details?id=com.aleksandar.cookml

### Where can I find the trained model?
- `models/ingredient-recognition/main-model` for the main model & `models/ingredient-recognition/tflite-model` for the TFLite version.

### How was the model trained?
- By using the Jupyter Notebook in `models/ingredient-recognition/jupyter-notebook`. Unless a GPU is available, I recommend Google Colab. However, even in Google Colab a single epoch might take as long as ~400 seconds. Because of that, the model was trained in the GCP (Google Cloud Platform) on a single NVIDIA Tesla K80 instance.

### What python packages do I need?
- All of the needed python packages are listed in the `requirements.txt` file.

### How can I test the model?
- Open the Jupyter Notebook and load all of the cells up to the `yolo_loss` method (the `yolo_loss` function should also be executed). Then, jump to the test the trained model section. And execute all of the cells. Changing the paths can allow you to test on new images.

## Credits
- The Jupyter Notebook reuses this YOLO implementation: https://github.com/experiencor/keras-yolo2
- Used photos: https://unsplash.com/photos/gILLi3SGFbw & https://unsplash.com/photos/oQvESMKUkzM

## External Libraries
- `TFLite`
- `Dagger`

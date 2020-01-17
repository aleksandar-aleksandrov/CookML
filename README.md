# CookML

![logo](assets/logo_big.png)

## Description

CookML is an Android mobile application which implements a recipe recommedation system with an embedded visual recognition of available food ingredients.

## Folders
- apk - A reusable apk version of the application
- assets - The different sizes of the logo of the application
- metrics - Some recorded metrics (execution time of the inference methods in ms)
- models - A trained YOLO model in the `.pb` format and `.tflite` format. In the `jupyter-notebook` subfolder, the notebook used for training the model can be found. In the `loss` subfolder, the loss during the training is saved.
- CookML - The actual Android application

## Credits
The Jupyter Notebook reuses this YOLO implementation: https://github.com/experiencor/keras-yolo2
Used photos: https://unsplash.com/photos/gILLi3SGFbw & https://unsplash.com/photos/oQvESMKUkzM

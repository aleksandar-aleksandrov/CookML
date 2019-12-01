from pathlib import Path
import os
import numpy as np
import tensorflow as tf
from matplotlib import pyplot as plt


class DataService:

    def __init__(self, data_path: str = 'dataset', batch_size: int = 32, img_height: int = 224,
                     img_width: int = 224, img_extension: str = 'png'):
        self.data_path: Path = Path(os.getcwd()).joinpath(data_path)
        self.batch_size: int = batch_size
        self.img_height = img_height
        self.img_width = img_width
        self.img_extension = img_extension

        self.image_count: int = len(list(self.data_path.glob(f'*/*.{self.img_extension}')))
        self.steps_per_epoch: int = np.ceil(self.image_count / self.batch_size)
        self.class_names: np.ndarray = np.array([item.name for item in self.data_path.glob('*') if item.name != "LICENSE.txt"])
        self.dataset = None

    def load_dataset(self):
        image_generator = tf.keras.preprocessing.image.ImageDataGenerator(rescale=1. / 255)

        self.dataset = image_generator.flow_from_directory(directory=str(self.data_path),
                                                           batch_size=self.batch_size,
                                                           shuffle=True,
                                                           target_size=(self.img_height, self.img_width),
                                                           classes=list(self.class_names),
                                                           class_mode="sparse")

    def show_batch(self):
        if not self.dataset:
            self.load_dataset()

        image_batch, label_batch = next(self.dataset)

        plt.figure(figsize=(10, 10))
        for n in range(25):
            ax = plt.subplot(5, 5, n + 1)
            plt.imshow(image_batch[n])
            plt.title(self.class_names[label_batch[n] == 1][0].title())
            plt.axis('off')

        plt.show()

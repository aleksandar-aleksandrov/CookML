from pathlib import Path
import pandas as pd
import numpy as np
import xml.etree.ElementTree as ET
import tensorflow as tf

dataset_path = Path(__file__).parent.absolute().joinpath('dataset')

df = pd.DataFrame(columns=['x', 'y'])

labels = [
    'beans',
    'cake',
    'candy',
    'cereal',
    'chips',
    'chocolate',
    'coffee',
    'corn',
    'fish',
    'flour',
    'honey',
    'jam',
    'juice',
    'milk',
    'nuts',
    'oil',
    'pasta',
    'rice',
    'soda',
    'spices',
    'sugar',
    'tea',
    'tomato_sauce',
    'vinegar',
    'water'
]

xr = np.array([])
yr = np.array([])

for class_dir in dataset_path.iterdir():
    for file in class_dir.iterdir():
        if(file.suffix == '.txt'):
            continue
        else:
            txt_file = Path(str(file).replace('.png', '.xml'))
            print(txt_file)
            tree = ET.parse(str(txt_file))
            root = tree.getroot()
            objs = []
            for child in root.iter('object'):
                obj = {}
                obj['class'] = child.find('name').text
                
                bndbox = child.find('bndbox')
                obj['xmax'] = float(bndbox.find('xmax').text)
                obj['xmin'] = float(bndbox.find('xmin').text)
                obj['ymax'] = float(bndbox.find('ymax').text)
                obj['ymin'] = float(bndbox.find('ymin').text)

                objs.append(obj)

            array = np.array([[0.]*10] * 49)
            
            for obj in objs:
                if obj['xmax'] > obj['xmin'] and obj['ymax'] > obj['ymin']:
                        C = 1

                        center_x = .5*(obj['xmin'] + obj['xmax'])
                        center_x = center_x / (224 / 7)
                        x = center_x - np.floor(center_x)
                        grid_x = int(np.floor(center_x))

                        center_y = .5*(obj['ymin'] + obj['ymax'])
                        center_y = center_y / (224 / 7)
                        y = center_y - np.floor(center_y)
                        grid_y = int(np.floor(center_y))

                        w = (obj['xmax'] - obj['xmin']) / 224
                        h = (obj['ymax'] - obj['ymin']) / 224

                        class_array = [0.] * 5
                        class_array[labels.index(obj['class'])] = 1.

                        array[grid_y*7 + grid_x] = class_array + [x] + [y] + [w] + [h] + [C]

            img = tf.keras.preprocessing.image.load_img(str(file))
            img_arr = tf.keras.preprocessing.image.img_to_array(img)
            print(img_arr.shape)
            xr = np.append(xr, img_arr)
            yr = np.append(yr, [array])
            df = df.append({'x': str(file), 'y': array}, ignore_index=True)
            break
    break

df.to_csv('dataset.csv')

r = pd.read_csv('dataset.csv')
b = r['y'][0].replace('\r', '').replace('[', '').replace('\n', '').split(']')
p = []
print('....................................................')
for sub_list in b:
    k = list([float(x) for x in sub_list.split(' ') if x])
    if k:
        p.append(k)


p = np.array(p)

import tensorflow as tf

g = tf.keras.preprocessing.image.ImageDataGenerator()
#a = g.flow_from_dataframe(dataframe=df, class_mode='raw', x_col='x', y_col='y')
print(xr.shape)
print(yr.shape)
#c = g.flow(xr, yr)
#print(next(c))
unique_indices = np.unique(np.nonzero(array)[0])
mask = np.isin(range(49), unique_indices)

print(mask)
print(array[mask])
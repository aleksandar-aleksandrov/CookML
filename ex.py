import tensorflow as tf
import numpy as np
S = 7

cell_x = tf.to_float(tf.reshape(tf.tile(tf.range(S), [S]), (1, S, S, 1, 1)))

cell_y = tf.transpose(cell_x, (0,2,1,3,4))
cell_grid = tf.tile(tf.concat([cell_x,cell_y], -1), [1, 1, 1, 1, 1])


with tf.Session() as sess:
    print(cell_grid.shape)
    #print(cell_grid.eval())
    grid = np.array([ [[float(x),float(y)]]*1   for y in range(S) for x in range(S)])
    print(grid)

    
    coord_scale = 2
    y_true = tf.Variable(tf.zeros((1, 2, 2)))
    y_true = y_true[0][0].assign(1.)
    print('--------------------')
    print(y_true.eval())
    #mask_shape = tf.shape(y_true)[:1]
    #coord_mask = tf.zeros(mask_shape)
    #coord_mask = tf.expand_dims(y_true[..., 4], axis=-1) * coord_scale
    print('-------------------------------')
    #print(coord_mask.eval())
    #print(coord_mask.shape)

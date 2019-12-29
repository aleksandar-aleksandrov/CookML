import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
x1 = np.linspace(0.0001, 0.99999, 50)
x2 = np.linspace(0.0001, 0.99999, 50)

y = 1/(1 - x1*x2)

ax.plot(x1, x2, y)
plt.plot(y)
plt.show()



def custom_loss(self, y_true, y_pred):
mask_shape = tf.shape(y_true)[:4]

cell_x = tf.to_float(tf.reshape(tf.tile(tf.range(self.grid_w), [self.grid_h]), (1, self.grid_h, self.grid_w, 1, 1)))

cell_y = tf.transpose(cell_x, (0,2,1,3,4))

cell_grid = tf.tile(tf.concat([cell_x,cell_y], -1), [self.batch_size, 1, 1, self.nb_box, 1])

coord_mask = tf.zeros(mask_shape)
conf_mask  = tf.zeros(mask_shape)
class_mask = tf.zeros(mask_shape)

seen = tf.Variable(0.)
total_recall = tf.Variable(0.)

### adjust x and y      
pred_box_xy = tf.sigmoid(y_pred[..., :2]) + cell_grid

### adjust w and h
pred_box_wh = tf.exp(y_pred[..., 2:4]) * np.reshape(self.anchors, [1,1,1,self.nb_box,2])

### adjust confidence
pred_box_conf = tf.sigmoid(y_pred[..., 4])

### adjust class probabilities
pred_box_class = y_pred[..., 5:]

### adjust x and y
true_box_xy = y_true[..., 0:2] # relative position to the containing cell

### adjust w and h
true_box_wh = y_true[..., 2:4] # number of cells accross, horizontally and vertically

### adjust confidence
true_wh_half = true_box_wh / 2.

true_mins    = true_box_xy - true_wh_half
true_maxes   = true_box_xy + true_wh_half

pred_wh_half = pred_box_wh / 2.
pred_mins    = pred_box_xy - pred_wh_half
pred_maxes   = pred_box_xy + pred_wh_half       

intersect_mins  = tf.maximum(pred_mins,  true_mins)
intersect_maxes = tf.minimum(pred_maxes, true_maxes)

intersect_wh    = tf.maximum(intersect_maxes - intersect_mins, 0.)
intersect_areas = intersect_wh[..., 0] * intersect_wh[..., 1]

true_areas = true_box_wh[..., 0] * true_box_wh[..., 1]
pred_areas = pred_box_wh[..., 0] * pred_box_wh[..., 1]

union_areas = pred_areas + true_areas - intersect_areas
iou_scores  = tf.truediv(intersect_areas, union_areas)

true_box_conf = iou_scores * y_true[..., 4]

### adjust class probabilities
true_box_class = tf.argmax(y_true[..., 5:], -1)

### coordinate mask: simply the position of the ground truth boxes (the predictors)
coord_mask = tf.expand_dims(y_true[..., 4], axis=-1) * self.coord_scale

### confidence mask: penelize predictors + penalize boxes with low IOU
# penalize the confidence of the boxes, which have IOU with some ground truth box < 0.6
true_xy = self.true_boxes[..., 0:2]
true_wh = self.true_boxes[..., 2:4]

true_wh_half = true_wh / 2.
true_mins    = true_xy - true_wh_half
true_maxes   = true_xy + true_wh_half

pred_xy = tf.expand_dims(pred_box_xy, 4)
pred_wh = tf.expand_dims(pred_box_wh, 4)

pred_wh_half = pred_wh / 2.
pred_mins    = pred_xy - pred_wh_half
pred_maxes   = pred_xy + pred_wh_half    

intersect_mins  = tf.maximum(pred_mins,  true_mins)
intersect_maxes = tf.minimum(pred_maxes, true_maxes)

intersect_wh    = tf.maximum(intersect_maxes - intersect_mins, 0.)
intersect_areas = intersect_wh[..., 0] * intersect_wh[..., 1]

true_areas = true_wh[..., 0] * true_wh[..., 1]
pred_areas = pred_wh[..., 0] * pred_wh[..., 1]

union_areas = pred_areas + true_areas - intersect_areas
iou_scores  = tf.truediv(intersect_areas, union_areas)
best_ious = tf.reduce_max(iou_scores, axis=4)
conf_mask = conf_mask + tf.to_float(best_ious < 0.6) * (1 - y_true[..., 4]) * self.no_object_scale

# penalize the confidence of the boxes, which are reponsible for corresponding ground truth box
conf_mask = conf_mask + y_true[..., 4] * self.object_scale

### class mask: simply the position of the ground truth boxes (the predictors)
class_mask = y_true[..., 4] * tf.gather(self.class_wt, true_box_class) * self.class_scale       

no_boxes_mask = tf.to_float(coord_mask < self.coord_scale/2.)
seen = tf.assign_add(seen, 1.)

true_box_xy, true_box_wh, coord_mask = tf.cond(tf.less(seen, self.warmup_batches+1), 

lambda: [true_box_xy + (0.5 + cell_grid) * no_boxes_mask, 

true_box_wh + tf.ones_like(true_box_wh) * \

np.reshape(self.anchors, [1,1,1,self.nb_box,2]) * \

no_boxes_mask, 

tf.ones_like(coord_mask)],

lambda: [true_box_xy, 

true_box_wh,

coord_mask])



"""

Finalize the loss

"""

nb_coord_box = tf.reduce_sum(tf.to_float(coord_mask > 0.0))

nb_conf_box  = tf.reduce_sum(tf.to_float(conf_mask  > 0.0))

nb_class_box = tf.reduce_sum(tf.to_float(class_mask > 0.0))



loss_xy    = tf.reduce_sum(tf.square(true_box_xy-pred_box_xy)     * coord_mask) / (nb_coord_box + 1e-6) / 2.

loss_wh    = tf.reduce_sum(tf.square(true_box_wh-pred_box_wh)     * coord_mask) / (nb_coord_box + 1e-6) / 2.

loss_conf  = tf.reduce_sum(tf.square(true_box_conf-pred_box_conf) * conf_mask)  / (nb_conf_box  + 1e-6) / 2.

loss_class = tf.nn.sparse_softmax_cross_entropy_with_logits(labels=true_box_class, logits=pred_box_class)

loss_class = tf.reduce_sum(loss_class * class_mask) / (nb_class_box + 1e-6)



loss = tf.cond(tf.less(seen, self.warmup_batches+1), 

lambda: loss_xy + loss_wh + loss_conf + loss_class + 10,

lambda: loss_xy + loss_wh + loss_conf + loss_class)



if self.debug:

nb_true_box = tf.reduce_sum(y_true[..., 4])

nb_pred_box = tf.reduce_sum(tf.to_float(true_box_conf > 0.5) * tf.to_float(pred_box_conf > 0.3))



current_recall = nb_pred_box/(nb_true_box + 1e-6)

total_recall = tf.assign_add(total_recall, current_recall) 



loss = tf.Print(loss, [loss_xy], message='Loss XY \t', summarize=1000)

loss = tf.Print(loss, [loss_wh], message='Loss WH \t', summarize=1000)

loss = tf.Print(loss, [loss_conf], message='Loss Conf \t', summarize=1000)

loss = tf.Print(loss, [loss_class], message='Loss Class \t', summarize=1000)

loss = tf.Print(loss, [loss], message='Total Loss \t', summarize=1000)

loss = tf.Print(loss, [current_recall], message='Current Recall \t', summarize=1000)

loss = tf.Print(loss, [total_recall/seen], message='Average Recall \t', summarize=1000)



return loss
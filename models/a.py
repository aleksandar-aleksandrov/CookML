import numpy as np

S = 7
B = 2
C = 25

arr = np.ones((S*S, B*5 + C))

print(arr.shape)
arr[0] = [1] * 35
print(arr)


X = np.zeros((4975, 224, 224, 3))
Y = np.zeros((4975, S*S, B*5 + C))
Y[0] = arr
print(Y.shape)
print(Y[0:2])
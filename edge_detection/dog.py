import cv2
import numpy as np
 
img = cv2.imread("image.jpg", cv2.IMREAD_GRAYSCALE)
 
# Difference of Gaussians
blur1 = cv2.GaussianBlur(img, (9, 9), 2.0)
blur2 = cv2.GaussianBlur(img, (17, 17), 4.0)
dog = cv2.subtract(blur1, blur2)
 
# Normalize and threshold to get clean black/white
dog = cv2.normalize(dog, None, 0, 255, cv2.NORM_MINMAX)
_, edges = cv2.threshold(dog, 3, 255, cv2.THRESH_BINARY)
 
cv2.imwrite("edges.jpg", edges)
print("Done → edges.jpg")
 


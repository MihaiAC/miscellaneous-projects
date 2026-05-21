import cv2

img = cv2.imread('image.jpg')
edges = cv2.Canny(img, 60, 120)
cv2.imwrite("edges.jpg", edges)
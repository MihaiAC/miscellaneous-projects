from controlnet_aux import LineartDetector
from PIL import Image, ImageEnhance
from rembg import remove
import numpy as np
 
# Load and remove background
img = Image.open("image.jpg")
img_nobg = remove(img)  # RGBA
 
# Paste onto white background so lineart detector gets clean input
background = Image.new("RGB", img_nobg.size, (255, 255, 255))
background.paste(img_nobg, mask=img_nobg.split()[3])
img_clean = background
 
lineart = LineartDetector.from_pretrained("lllyasviel/Annotators")
edges = lineart(img_clean, detect_resolution=2048, image_resolution=1280, coarse=False)
edges.save("edges_final.jpg")
print("Saved edges_final.jpg")
 
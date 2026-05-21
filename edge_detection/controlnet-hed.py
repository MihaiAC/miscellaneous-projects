from controlnet_aux import HEDdetector
from PIL import Image

img = Image.open("image.jpg")
hed = HEDdetector.from_pretrained("lllyasviel/Annotators")
edges = hed(img)
edges.save("edges.jpg")
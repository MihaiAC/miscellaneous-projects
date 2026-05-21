from controlnet_aux import LineartDetector
from PIL import Image, ImageEnhance
 
img = Image.open("image.jpg")
 
# Boost contrast slightly before detection
img_enhanced = ImageEnhance.Contrast(img).enhance(1.5)
 
lineart = LineartDetector.from_pretrained("lllyasviel/Annotators")
 
configs = [
    ("edges_1600.jpg",          {"detect_resolution": 1600, "image_resolution": 1280, "coarse": False}),
    ("edges_2048.jpg",          {"detect_resolution": 2048, "image_resolution": 1280, "coarse": False}),
    ("edges_contrast_fine.jpg", {"detect_resolution": 1280, "image_resolution": 1280, "coarse": False}),
]
 
for filename, kwargs in configs:
    src = img_enhanced if "contrast" in filename else img
    edges = lineart(src, **kwargs)
    edges.save(filename)
    print(f"Saved {filename}")
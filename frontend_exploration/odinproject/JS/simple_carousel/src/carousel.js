export class Carousel {
  constructor(carousel_id) {
    this.carousel = document.querySelector(carousel_id);
    this.carouselInner = this.carousel.firstElementChild;
    this.interval = setInterval(() => this.selectNextImage(), 5000);
  }

  getActiveImageIdx(images) {
    let activeImageIdx = -1;
    for (let index = 0; index < images.length; index++) {
      if (images[index].classList.contains("active")) {
        activeImageIdx = index;
        break;
      }
    }

    if (activeImageIdx < 0) {
      const error_msg =
        "Carousel contains no images or none of the images is marked as active.";
      console.error(error_msg);
      throw new Error(error_msg);
    }

    return activeImageIdx;
  }

  // Called after the user presses one of the manual controls.
  resetTimer() {
    clearInterval(this.interval);
    this.interval = setInterval(() => this.selectNextImage(), 5000);
  }

  selectNextImage() {
    const images = this.carouselInner.children;
    let activeImageIdx = this.getActiveImageIdx(images);

    console.log(images[activeImageIdx].classList);

    // Remove active from the current image.
    images[activeImageIdx].classList.remove("active");

    if (activeImageIdx < images.length - 1) {
      activeImageIdx += 1;
    } else {
      activeImageIdx = 0;
    }

    images[activeImageIdx].classList.add("active");
  }

  selectPreviousImage() {
    const images = this.carouselInner.children;
    let activeImageIdx = this.getActiveImageIdx(images);

    console.log(images[activeImageIdx].classList);

    // Remove active from the current image.
    images[activeImageIdx].classList.remove("active");

    if (activeImageIdx > 0) {
      activeImageIdx -= 1;
    } else {
      activeImageIdx = images.length - 1;
    }

    images[activeImageIdx].classList.add("active");
  }
}

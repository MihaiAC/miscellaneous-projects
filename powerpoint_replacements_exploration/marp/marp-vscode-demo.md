---
marp: true
theme: uncover
paginate: true
size: 16:9
backgroundColor: #111
style: |
  section {
    color: chartreuse;
  }

  section::after {
    color: chartreuse !important;
    text-shadow: none !important;
  }
---

# Welcome to Marp

Writing PowerPoint-style presentations in Markdown wooooo

---

You should use --- to separate slides

---

![Image alt text](https://marp.app/assets/marp.svg)

---

Quicksort in Vanilla JS

```js
function quicksort(arr) {
  if (arr.length < 2) return arr;

  const pivot = arr[0];
  const left = arr.slice(1).filter((x) => x < pivot);
  const right = arr.slice(1).filter((x) => x >= pivot);

  return [...quicksort(left), pivot, ...quicksort(right)];
}
```

---

#### Can we maybe make a video embed work OOOOooooo

```html
<iframe
  width="560"
  height="315"
  src="https://www.youtube.com/embed/dQw4w9WgXcQ"
  frameborder="0"
  allowfullscreen
></iframe>
```

No, we cannot! At least not in the VSCode extension, which disallows iframes for security reasons I guess.

---

- Can we make a list?
  - Who knows man?
    - How long does this go on?

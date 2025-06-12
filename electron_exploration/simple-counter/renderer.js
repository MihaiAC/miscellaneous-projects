let count = 0;
const countEl = document.getElementById("count");
const button = document.getElementById("increment");

button.addEventListener("click", () => {
  count++;
  countEl.textContent = count;
});

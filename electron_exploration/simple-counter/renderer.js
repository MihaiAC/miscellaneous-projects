let count = 0;
const counterEl = document.getElementById("counter");

function update() {
  counterEl.textContent = count;
}

// Listen for the global shortcut event
window.electronAPI.onIncrement(() => {
  count++;
  update();
});

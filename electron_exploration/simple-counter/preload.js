const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("electronAPI", {
  onIncrement: (cb) => ipcRenderer.on("increment", cb),
});

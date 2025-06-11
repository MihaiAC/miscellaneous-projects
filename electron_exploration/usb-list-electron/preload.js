const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("usbAPI", {
  getDevices: () => ipcRenderer.invoke("get-usb-devices"),
});

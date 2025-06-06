const { app, BrowserWindow, ipcMain } = require("electron");
const path = require("node:path");

// Loads index.html into a BrowserWindow instance.
const createWindow = () => {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
    },
  });

  win.loadFile("index.html");
};

// Create window when the app is ready.
app.whenReady().then(() => {
  ipcMain.handle("ping", () => "pong");
  createWindow();
});

// Quit the app when all windows are closed.
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

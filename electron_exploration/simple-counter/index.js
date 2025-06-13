const { app, BrowserWindow, ipcMain, globalShortcut } = require("electron");
const path = require("path");

let win;

function createWindow() {
  win = new BrowserWindow({
    width: 200,
    height: 150,
    transparent: true,
    frame: false,
    alwaysOnTop: true,
    resizable: false,
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
    },
  });

  win.loadFile("index.html");
}

// Register global shortcuts after app is ready
app.whenReady().then(() => {
  createWindow();

  // Increment
  globalShortcut.register("Ctrl+Alt+I", () => {
    if (win && !win.isDestroyed()) {
      win.webContents.send("increment");
    }
  });

  // Close window
  globalShortcut.register("Ctrl+Alt+Q", () => {
    if (win && !win.isDestroyed()) {
      win.close();
    }
  });
});

// Clean up
app.on("will-quit", () => {
  globalShortcut.unregisterAll();
});

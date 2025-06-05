const { app, BrowserWindow } = require("electron");

// Loads index.html into a BrowserWindow instance.
const createWindow = () => {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
  });

  win.loadFile("index.html");
};

// Create window when the app is ready.
app.whenReady().then(() => {
  createWindow();
});

// Quit the app when all windows are closed.
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

const { getVendorName, getProductName } = require("./usb-id-parser.js");
const { app, BrowserWindow, ipcMain } = require("electron");
const usb = require("usb");
const path = require("path");

function createWindow() {
  const window = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
    },
  });

  window.loadFile("index.html");
}

app.whenReady().then(createWindow);

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

ipcMain.handle("get-usb-devices", async () => {
  try {
    const devices = usb.getDeviceList();

    // Map over devices and look up names
    const deviceDetails = devices.map((device) => {
      const vid = device.deviceDescriptor.idVendor;
      const pid = device.deviceDescriptor.idProduct;

      const vendorName = getVendorName(vid);
      const productName = getProductName(vid, pid);

      const vidHex = vid.toString(16).padStart(4, "0");
      const pidHex = pid.toString(16).padStart(4, "0");

      return {
        vendorId: vidHex,
        productId: pidHex,
        manufacturer: vendorName || `Unknown Vendor (${vidHex})`,
        product: productName || `Unknown Product (${pidHex})`,
      };
    });

    return deviceDetails;
  } catch (error) {
    console.error("Failed to list USB devices:", error);
    return [];
  }
});

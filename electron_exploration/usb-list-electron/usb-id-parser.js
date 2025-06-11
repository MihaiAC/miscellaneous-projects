const fs = require("fs");
const path = require("path");

let vendors = {};
let products = {};

function parseIdsFile() {
  try {
    const data = fs.readFileSync(path.join(__dirname, "usb.ids"), "utf8");
    const lines = data.split("\n");

    let currentVendorId = null;

    lines.forEach((line) => {
      // Ignore comments, empty lines
      if (line.startsWith("#") || line.trim() === "") {
        return;
      }

      // Vendor lines are not indented
      if (!line.startsWith("\t")) {
        const parts = line.split(/\s+/);
        const vendorId = parts[0];
        const vendorName = parts.slice(1).join(" ");
        if (vendorId && vendorName) {
          currentVendorId = vendorId.toLowerCase();
          vendors[currentVendorId] = vendorName;
        }
      }
      // Product line = indented by tab
      else if (
        line.startsWith("\t") &&
        !line.startsWith("\t\t") &&
        currentVendorId
      ) {
        const parts = line.trim().split(/\s+/);
        const productId = parts[0];
        const productName = parts.slice(1).join(" ");
        if (productId && productName) {
          // Create a unique key for the product: "vendorid_productid"
          products[`${currentVendorId}_${productId.toLowerCase()}`] =
            productName;
        }
      }
    });
    console.log(
      `[USB Parser] Loaded ${Object.keys(vendors).length} vendors and ${
        Object.keys(products).length
      } products.`
    );
  } catch (error) {
    console.error("Fatal Error: Could not read or parse usb.ids file.", error);
    vendors = {};
    products = {};
  }
}

// Parse the file once when the module is loaded
parseIdsFile();

function getVendorName(vendorId) {
  const vid = vendorId.toString(16).padStart(4, "0").toLowerCase();
  return vendors[vid] || null;
}

function getProductName(vendorId, productId) {
  const vid = vendorId.toString(16).padStart(4, "0").toLowerCase();
  const pid = productId.toString(16).padStart(4, "0").toLowerCase();
  return products[`${vid}_${pid}`] || null;
}

module.exports = { getVendorName, getProductName };

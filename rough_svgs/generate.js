const { JSDOM } = require("jsdom");
const rough = require("roughjs");
const fs = require("fs");
const path = require("path");

const dom = new JSDOM("");
const { document } = dom.window;

for (let idx = 1; idx <= 4; idx++) {
  const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
  svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
  svg.setAttribute("width", "80");
  svg.setAttribute("height", "30");
  svg.setAttribute("viewBox", "0 0 80 30");

  const rc = rough.svg(svg);
  const node = rc.rectangle(2, 2, 76, 26, {
    roughness: 2.5,
    fillWeight: 1.5,
    fillStyle: "hachure",
    stroke: "none",
    fill: "currentColor",
    seed: idx * 137,
  });

  svg.appendChild(node);

  fs.writeFileSync(
    path.join(__dirname, `rough${idx}.svg`),
    svg.outerHTML,
    "utf8",
  );
  console.log(`Generated rough${idx}.svg`);
}

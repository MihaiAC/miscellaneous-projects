### Process model
In Chromium, every tab has a different process (didn't know that, damn).
Main process = entry point + creates/manages application windows with the BrowserWindow module + manages application's lifecycle

Each instance of the BrowserWindow class = application window = loads web page in a separate renderer process. Can interact with this from the main process using the windows' `webContents` object.

Preload scripts exist, similar to React.

Any Electron app can spawn multiple child utility processes. These processes run in a Node.js env, => can import node modules. Can run:
- untrusted services;
- CPU intensive tasks;
- crash prone components;
Utility processes (`UtilityProcess` API) can establish communication channels with a renderer process through `MessagePort`.

### chrome-sandbox permissions
The binary needs to be owned by root so the Electron app (non-root) can use it to create a sandboxed environment for its child processes. 

The Electron app itself should not run as root.

Fix:
`sudo chown root:root ./node_modules/electron/dist/chrome-sandbox`
`sudo chmod 4755 ./node_modules/electron/dist/chrome-sandbox`

### Loading a web page into a BrowserWindow
In Electron, each window -> local/remote web page.

### Electron fires certain events at certain points
Launch your window when the app is ready with
`app.whenReady().then((...etc...))`


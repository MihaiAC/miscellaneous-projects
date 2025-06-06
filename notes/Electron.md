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

### Preload scripts
Electron's main process = Node.js env that has full OS access.
On top of Electron modules, can access Node.js builtins + npm packages.
Renderer processes run web pages and do not run Node.js by default for security reasons.
To bridge different process types together => preload script.

A preload script **has access to HTML DOM + limited subset of Node.js, Electron APIs.**
Preload scripts are also sandboxed. They have access to:
- Electron modules: renderer process modules;
- Node.js modules: events, timers, url;
- Polyfilled globals: Buffer, process, clearImmediate, setImmediate.
Preload scripts are injected before a web page loads.


#### Communicating between processes
Main and renderer processes have distinct responsibilities, are not interchangeable.
-> Node.js API cannot be accessed from the renderer process;
-> HTML DOM cannot be accessed from the main process;
Interprocess comm = IPC
`ipcMain`, `ipcRenderer`

To send a message from web page to main process:
- set up a main process handler with `ipcMain.handle`;
- expose a function that calls `ipcRenderer.invoke` to trigger the handler in your preload script;
```
[ Renderer process ]
 ↓  calls window.versions.ping()
[ Preload script ]
 ↓  uses ipcRenderer.invoke('ping')
[ Main process ]
 ↓  ipcMain.handle('ping', () => return 'pong')
[ Back to renderer ]
```

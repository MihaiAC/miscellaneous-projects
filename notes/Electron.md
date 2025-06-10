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

### Packaging the app
```
npm install --save-dev @electron-forge/cli
npx electron-forge import

npm run make #after installing rpm
```

### Performance
Profiling a "require":
`node --cpu-prof --heap-prof -e "require('request')"` + chrome developer tools
1. Carefully consider what modules you are installing:
	1. Size of dependencies.
	2. How expensive is importing the module (e.g: reading a huge JSON file).
	3. How expensive is it for the package to perform the action(s) we're interested in.
2. Stagger imports. Loading everything up top is not a good idea.
   e.g: Code first loads the text, then the highlighting and other bells and whistles
3. Don't block main thread + UI thread:
	1. CPU-heavy => worker threads or spawn a dedicated process.
	2. Avoid sync IPC (main and worker wait for one another => UI freezes) and `@electron/remote` (sync IPC to let renderer access main process APIs) as much as possible.
	3. No blocking I/O in main process.
4. Don't block the renderer process.
	1. `requestIdleCallback()` -> queue up small tasks as soon as the process enters an idle period; (無為 callback :P)
	2. Web workers, again.
5. Unnecessary polyfills (caniuse.com)
6. Unnecessary network requests.
	1. Bundle things instead of downloading them.
	2. Ideally - app shouldn't require the internet.
7. Bundle code.
8. Don't build a menu when you don't need one (`Menu.setApplicationMenu(null)`).

### Security
1. Use https, wss, ftps, etc. D'OH.
2. Do not enable Node.js integration for remote content (XSS danger).
3. Do not disable context isolation.
4. Enable process sandboxing (the thing I have to do anyway every time I run an Electron app on Ubuntu).
5. ????? Electron automatically approves all permission requests unless you configure a custom handler.
6. `webSecurity` - what does it do? enforces CORS, blocks loading mixed content (http inside of https??), restricts access to local files from remote content, blocks iframes from accessing parent content unless same origin.
   
   Http inside https = you're on a https page and it's trying to load things by http => mixed content.
7. Can define a CSP (content security policy).
8. `allowRunningInsecureContent` -> why is it even an option? hmm actually nvm, I have an idea why.
9. Do not enable experimental features or "blink" features.
10. Do not `allowpopups` for WebViews - do it only if needed (window opening another window basically).
11. Disable or limit navigation with `will-navigate` handler.
12. Declare a window open handler + deny most requests.
13. Careful with `shell.openExternal`.
14. Validate the sender of all IPC messages. 
    Possible attack vectors here:
    - compromised renderer through malicious extension / untrusted content;
    - no access control = privilege escalation;
    - malicious preload/injected JS;
15. Do not use `file://` but use a custom `app://` protocol. If you use file, attacker can access everything on the system. With custom, you define exactly what gets loaded + sanitize for '../' etc. Use `protocol.handle(scheme, handler)`
16. Some Electron features you don't need can be disabled ([fuses](https://www.electronjs.org/docs/latest/tutorial/fuses))
17. Do not expose Electron APIs to untrusted web content in preload scripts.
```js
// Bad
contextBridge.exposeInMainWorld('electronAPI', {  on: ipcRenderer.on})

// Also bad
contextBridge.exposeInMainWorld('electronAPI', {  onUpdateCounter: (callback) => ipcRenderer.on('update-counter', callback)})

// Good
contextBridge.exposeInMainWorld('electronAPI', {  onUpdateCounter: (callback) => ipcRenderer.on('update-counter', (_event, value) => callback(value))})
```
The idea here is to prevent access to event. Just pass value to the callback.
Way to control the data that hits the callback (may still be malicious if not careful).
First example allows access to ipcRenderer.
Second example allows arbitrary event execution (?).
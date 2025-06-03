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


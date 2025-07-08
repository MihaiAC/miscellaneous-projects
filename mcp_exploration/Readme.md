Exploring the MCP protocol with a local server that reads the time (running in Docker), a remote Weather API MCP server and Groq.

### Running the time service container

`docker build -t time-server .`
`docker run -i --rm time-server`

Can test the server by pasting JSON messages in the terminal. Examples:

- Initialise the connection:
  `{"jsonrpc": "2.0", "id": 1, "method": "initialize", "params": {}}`

- List available tools:
  `{"jsonrpc": "2.0", "id": 2, "method": "tools/list", "params": {}}`

- Call get_time:
  `{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "get_time", "arguments": {}}}`

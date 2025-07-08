"""
MCP Client for Groq

This is a minimal, single-shot client. It takes a user prompt from the
command line, connects to the running time_server, and uses Groq to
orchestrate a tool call to get a final answer.
"""

import asyncio
import json
import os
import sys
from groq import AsyncGroq
from dotenv import load_dotenv

TIME_SERVER_HOST = "localhost"
TIME_SERVER_PORT = 8888


class MCPConnection:
    def __init__(self, reader, writer, server_name):
        self.reader = reader
        self.writer = writer
        self.server_name = server_name
        self._next_id = 1

    async def _send_request(self, method, params=None):
        """
        Constructs, sends, and awaits a response for a JSON-RPC request.
        This is the core communication method.
        """
        # Create the JSON-RPC request object.
        request = {
            "jsonrpc": "2.0",
            "id": self._next_id,
            "method": method,
            "params": params or {},
        }
        self._next_id += 1

        # Convert the request to a JSON string and send it over the network,
        # ensuring it's newline-terminated as per the server's expectation.
        message = json.dumps(request)
        self.writer.write(f"{message}\n".encode("utf-8"))
        await self.writer.drain()

        # Wait for and read the server's response.
        response_line = await self.reader.readline()
        if not response_line:
            raise ConnectionError(f"Connection to {self.server_name} closed.")
        return json.loads(response_line)

    async def list_tools(self):
        """Fetches the list of available tools from the server."""
        print(f"--> Fetching tools from {self.server_name}...", file=sys.stderr)
        response = await self._send_request("tools/list")
        if "error" in response:
            raise RuntimeError(f"Error listing tools: {response['error']}")
        return response["result"]["tools"]

    async def call_tool(self, name, arguments):
        """Sends a request to execute a specific tool on the server."""
        print(f"--> Calling tool '{name}'...", file=sys.stderr)
        params = {"name": name, "arguments": arguments}
        response = await self._send_request("tools/call", params)
        if "error" in response:
            return response["error"]
        return response["result"]


async def main():
    """The main entry point for the client script."""

    # Load environment variables from a .env file if it exists.
    load_dotenv()

    # Get user input.
    if len(sys.argv) < 2:
        print(f'Usage: python {sys.argv[0]} "<your question>"', file=sys.stderr)
        sys.exit(1)
    user_prompt = sys.argv[1]

    # Get Groq key and declare a client.
    groq_api_key = os.environ.get("GROQ_API_KEY")
    if not groq_api_key:
        print("Error: GROQ_API_KEY environment variable not set.", file=sys.stderr)
        sys.exit(1)

    groq_client = AsyncGroq(api_key=groq_api_key)
    writer = None

    try:
        # Connect to the time server and get tools.
        print(
            f"--> Connecting to time_server at {TIME_SERVER_HOST}:{TIME_SERVER_PORT}...",
            file=sys.stderr,
        )

        reader, writer = await asyncio.open_connection(
            TIME_SERVER_HOST, TIME_SERVER_PORT
        )

        time_client = MCPConnection(reader, writer, "time_server")

        # MCP servers must be initialized. We send the request but don't need the response.
        await time_client._send_request("initialize")
        tools = await time_client.list_tools()

        # Ask Groq for a plan.
        messages = [
            {
                "role": "system",
                "content": "You are a helpful assistant. Use tools when necessary.",
            },
            {"role": "user", "content": user_prompt},
        ]

        print("--> Asking Groq for a plan...", file=sys.stderr)
        chat_completion = await groq_client.chat.completions.create(
            messages=messages,
            model="llama3-70b-8192",
            tools=tools,
            tool_choice="auto",
        )

        response_message = chat_completion.choices[0].message

        # Execute the plan.
        if not response_message.tool_calls:
            # If Groq provides a direct answer, print it and exit.
            print(response_message.content)
            return

        # If Groq wants to use a tool, execute the call.
        messages.append(response_message)
        tool_call = response_message.tool_calls[0]
        tool_output = await time_client.call_tool(
            tool_call.function.name, json.loads(tool_call.function.arguments)
        )
        messages.append(
            {
                "role": "tool",
                "tool_call_id": tool_call.id,
                "content": json.dumps(tool_output),
            }
        )

        # Get the final answer from Groq.
        print(
            "--> Sending tool result back to Groq for a final answer...",
            file=sys.stderr,
        )
        final_completion = await groq_client.chat.completions.create(
            messages=messages, model="llama3-70b-8192"
        )
        print(final_completion.choices[0].message.content)

    except (OSError, ConnectionError) as e:
        print(f"\nError: Could not connect to time_server: {e}", file=sys.stderr)
        print("Please ensure the time_server container is running.", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"\nAn unexpected error occurred: {e}", file=sys.stderr)
    finally:
        # Ensure the network connection is always closed, even if errors occur.
        if writer:
            writer.close()
            await writer.wait_closed()


if __name__ == "__main__":
    # This block runs the main asynchronous function.
    asyncio.run(main())

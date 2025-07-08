"""
Simple Time MCP Server - should return current time
"""

import pytz
import asyncio
import sys
import json
from datetime import timezone, datetime


class TimeMCPServer:
    def __init__(self):
        # Using a dispatch table (dictionary) is a clean, scalable way to map
        # method names to the functions that handle them. It avoids long if/elif/else chains.
        self.method_handlers = {
            "initialize": self.handle_initialize,
            "tools/list": self.handle_tools_list,
            "tools/call": self.handle_tools_call,
        }
        # A dispatch table for tools makes it easy to add new tool functions.
        self.tool_handlers = {
            "get_time": self.get_time,
        }
        self.tools = {
            "get_time": {
                "type": "function",
                "function": {
                    "name": "get_time",
                    "description": "Get current time in various formats and timezones",
                    "parameters": {
                        "type": "object",
                        "properties": {
                            "timezone": {
                                "type": "string",
                                "description": "Timezone (e.g., 'UTC', 'US/Eastern', 'Europe/London')",
                                "default": "UTC",
                            },
                            "format": {
                                "type": "string",
                                "description": "Time format ('iso', 'human', 'unix')",
                                "default": "human",
                            },
                        },
                    },
                },
            }
        }

    async def handle_initialize(self, params):
        return {
            "protocolVersion": "2025-07-08",
            "capabilities": {"tools": {}},
            "serverInfo": {"name": "time_mcp_server", "version": "1.0.0"},
        }

    async def handle_tools_list(self, params):
        return {"tools": list(self.tools.values())}

    async def handle_tools_call(self, params):
        tool_name = params.get("name")
        args = params.get("arguments", {})

        handler = self.tool_handlers.get(tool_name)
        if not handler:
            raise ValueError(f"Unknown tool: {tool_name}")

        return await handler(args)

    async def get_time(self, args):
        timezone_name = args.get("timezone", "UTC")
        time_format = args.get("format", "human")

        try:
            if timezone_name == "UTC":
                timezone_obj = timezone.utc
            else:
                timezone_obj = pytz.timezone(timezone_name)

            now = datetime.now(timezone_obj)

            if time_format == "iso":
                time_str = now.isoformat()
            elif time_format == "unix":
                time_str = str(int(now.timestamp()))
            else:
                time_str = now.strftime("%Y-%m-%d %H:%M:%S %Z")

            return {
                "content": [
                    {
                        "type": "text",
                        "text": f"Current time in {timezone_name}: {time_str}",
                    }
                ]
            }
        except Exception as exc:
            return {
                "content": [
                    {"type": "text", "text": f"Error getting time: {str(exc)}"}
                ],
                "isError": True,
            }

    async def handle_message(self, message):
        try:
            msg_id = None
            data = json.loads(message)
            method = data.get("method")
            params = data.get("params", {})
            msg_id = data.get("id")

            # Look up the handler function from our dispatch table.
            handler = self.method_handlers.get(method)
            if not handler:
                raise ValueError(f"Unknown method: {method}")

            result = await handler(params)
            return json.dumps({"jsonrpc": "2.0", "id": msg_id, "result": result})
        except Exception as exc:
            return json.dumps(
                {
                    "jsonrpc": "2.0",
                    "id": msg_id,
                    "error": {"code": -32603, "message": str(exc)},
                }
            )

    async def handle_connection(self, reader, writer):
        addr = writer.get_extra_info("peername")
        print(f"Received connection from {addr}", file=sys.stderr)
        try:
            while True:
                line = await reader.readline()

                if not line:
                    break

                response = await self.handle_message(line.decode().strip())
                writer.write(f"{response}\n".encode("utf-8"))
                await writer.drain()
        finally:
            print(f"Closed connection from {addr}", file=sys.stderr)
            writer.close()
            await writer.wait_closed()

    async def run(self):
        print("Time MCP server starting...", file=sys.stderr)
        while True:
            try:
                server = await asyncio.start_server(
                    self.handle_connection, "0.0.0.0", 8888
                )

                # For this simple server, we can assume it binds to one address.
                addr = server.sockets[0].getsockname()
                print(f"Serving on {addr!r}", file=sys.stderr)
                async with server:
                    await server.serve_forever()

            except Exception as exc:
                print(f"Error: {exc}", file=sys.stderr)
                break


if __name__ == "__main__":
    asyncio.run(TimeMCPServer().run())

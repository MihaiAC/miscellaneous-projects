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
        self.tools = {
            "get_time": {
                "name": "get_time",
                "description": "Get current time in various formats and timezones",
                "inputSchema": {
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

        if tool_name == "get_time":
            return await self.get_time(args)
        else:
            raise ValueError(f"Unknown tool: {tool_name}")

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
                "content": [{"type": "text", "text": f"Error getting time: {str(e)}"}],
                "isError": True,
            }

    async def handle_message(self, message):
        try:
            data = json.loads(message)
            method = data.get("method")
            params = data.get("params", {})
            msg_id = data.get("id")

            if method == "initialize":
                result = await self.handle_initialize(params)
            elif method == "tools/list":
                result = await self.handle_tools_list(params)
            elif method == "tools/call":
                result = await self.handle_tools_call(params)
            else:
                raise ValueError(f"Unknown method: {method}")

            return json.dumps({"jsonrpc": "2.0", "id": msg_id, "result": result})
        except Exception as exc:
            return json.dumps(
                {
                    "jsonrpc": "2.0",
                    "id": data.get("id") if "data" in locals() else None,
                    "error": {"code": -32603, "message": str(exc)},
                }
            )

    async def run(self):
        print("Time MCP server starting...", file=sys.stderr)
        while True:
            try:
                line = await asyncio.get_event_loop().run_in_executor(
                    None, sys.stdin.readline
                )

                if not line:
                    break

                line = line.strip()

                if line:
                    response = await self.handle_message(line)
                    print(response, flush=True)

            except KeyboardInterrupt:
                break

            except Exception as exc:
                print(f"Error: {exc}", file=sys.stderr)


if __name__ == "__main__":
    asyncio.run(TimeMCPServer().run())

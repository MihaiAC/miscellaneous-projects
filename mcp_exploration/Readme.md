Exploring the MCP protocol with a decoupled `time_server` and a Groq-powered client.

The `time_server` runs as an independent TCP server in a Docker container. The `mcp_client` connects to it to provide tool-use capabilities to the Groq LLM.

### How to Run

**Terminal 1: Setup and Run the Server**

1.  **Build the Docker Image:**

    ```bash
    docker build -t time-server .
    ```

2.  **Run the Time Server:**
    Run the server and map the container's port 8888 to your local machine's port 8888. The server will now be running and waiting for connections.
    ```bash
    docker run --rm -p 8888:8888 time-server
    ```
    You will see a "Serving on..." message.

**Terminal 2: Setup and Run the Client**

1.  **Create and Activate a Virtual Environment:**
    It's best practice to use a virtual environment to manage project dependencies.

    ```bash
    python3 -m venv venv
    source venv/bin/activate
    ```

2.  **Install Dependencies:**

    ```bash
    pip install -r requirements.txt
    ```

3.  **Set your Groq API Key:**
    Get a key from the [Groq Console](https://console.groq.com/keys) and set it as an environment variable.

    ```
    GROQ_API_KEY='your-api-key-here'
    ```

4.  **Run the Client:**
    Run the client, passing your question as a command-line argument.
    ```bash
    python mcp_client.py "What time is it in Tokyo in ISO format?"
    ```

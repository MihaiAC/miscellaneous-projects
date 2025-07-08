FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY time_server.py .
EXPOSE 8888
CMD ["python", "time_server.py"]
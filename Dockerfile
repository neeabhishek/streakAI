FROM python:3.8-slim
WORKDIR /app
COPY . /app
RUN pip3 install -r requrement.txt
EXPOSE 5000
CMD ["python3", "app.py"]
# Use a different, stable version of Python
FROM python:3.12

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install core dependencies.
#RUN apt-get update && apt-get install -y libpq-dev build-essential

# Install any needed packages specified in requirements.txt
RUN pip install -r requirements.txt

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Define environment variable
ENV FLASK_APP=main.py

# Run app.py when the container launches
CMD ["flask", "run", "--host=0.0.0.0", "--port=8081"]

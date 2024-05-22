#!/bin/bash
set -e

# Echo deployment started
echo "Deployment started..."
# Pull the latest version of the app
echo "Pulling latest changes from repository..."
git pull origin dev

# Build the Spring Boot application
echo "Building Spring Boot application..."
./mvnw clean package

# Deployment finished
echo "Deployment finished successfully!"

# Path to your Spring Boot JAR file
SPRING_APP_JAR="/opt/SSec-0.0.1-SNAPSHOT.jar

# Check if the JAR file exists
if [ -f "$SPRING_APP_JAR" ]; then
    echo "Spring Boot application JAR file found. Restarting application..."
    systemctl restart your-spring-boot-service  # Ganti dengan perintah untuk memulai ulang layanan Spring Boot Anda
else
    echo "Spring Boot application JAR file not found. Deployment might have failed."
fi

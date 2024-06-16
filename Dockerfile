# Menggunakan image resmi openjdk sebagai base image
FROM openjdk:17-jdk-slim

# Menetapkan variabel lingkungan untuk aplikasi Spring Boot
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAR_FILE=app.jar

# Direktori kerja di dalam container
WORKDIR /app

# Menyalin file JAR yang telah dibangun dari host ke dalam container
COPY target/*.jar $JAR_FILE

# Menambahkan metadata untuk versi Docker dan image
LABEL maintainer="amsalsiregar12@gmail.com" \
    version="1.0" \
    description="Docker image for Spring Boot 3 application with Java JDK 17"

# Menjalankan aplikasi Spring Boot
ENTRYPOINT ["java","-jar","/app/app.jar"]

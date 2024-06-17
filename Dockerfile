# # Menggunakan image resmi openjdk sebagai base image
FROM openjdk:17-jdk-slim AS build

# Menetapkan direktori kerja
WORKDIR /app

# Menyalin file Maven wrapper dan pom.xml ke direktori kerja
COPY .mvn/ .mvn
COPY mvnw mvnw
COPY pom.xml .

# Menambah izin eksekusi ke mvnw
RUN chmod +x mvnw

# Menjalankan perintah Maven untuk mengunduh dependensi
RUN ./mvnw dependency:go-offline

# Menyalin seluruh isi proyek ke direktori kerja
COPY src ./src

# Menjalankan perintah Maven untuk build dengan plugin jar
RUN ./mvnw clean package -DskipTests

# Tahap runtime untuk menjalankan aplikasi
FROM openjdk:17-jdk-slim
WORKDIR /app

# Menyalin file JAR dari tahap build ke tahap runtime
COPY --from=build /app/target/*.jar app.jar

# Menjalankan aplikasi Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar", "-Dspring-boot.run.arguments=--seeder=menu,permission,role,user"]

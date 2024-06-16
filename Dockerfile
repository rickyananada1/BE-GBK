# Menggunakan image resmi openjdk sebagai base image
FROM openjdk:17-jdk-slim AS build

# Menetapkan direktori kerja
WORKDIR /app

# Menyalin file Maven wrapper dan pom.xml ke direktori kerja
COPY .mvn/ .mvn
COPY pom.xml .

# Menjalankan perintah Maven untuk mengunduh dependensi
RUN ./mvnw dependency:go-offline

# Menyalin seluruh isi proyek ke direktori kerja
COPY src ./src

# Menjalankan perintah Maven untuk build dan menjalankan seeder
RUN ./mvnw clean spring-boot:run -Dspring-boot.run.arguments=--seeder=menu,permission,role,user

# Tahap runtime untuk menjalankan aplikasi
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Menjalankan aplikasi Spring Boot
ENTRYPOINT ["java","-jar","/app/app.jar"]

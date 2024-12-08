# Use the official Maven image to build the application
FROM maven:3.8.5-openjdk-17  AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

## Copy the jar file from the target directory
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "app.jar"]

#docker build -t lab2-app .
#docker run -p 8080:8080 lab2-app
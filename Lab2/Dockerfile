# Use a base image with Java installed
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the jar file from the target directory
COPY target/Lab2-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]

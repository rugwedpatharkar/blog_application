# Use the base image with JDK 17 installed
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from the target directory to the container
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "app.jar"]

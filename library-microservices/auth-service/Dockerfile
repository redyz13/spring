# Use a base image with OpenJDK
FROM openjdk:21-jdk-slim

# Copy the JAR file into the Docker image
ARG JAR_FILE=target/auth-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} auth-service.jar

# Expose the Books Service port
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]

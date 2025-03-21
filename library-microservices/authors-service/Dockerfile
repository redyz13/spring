# Use a base image with OpenJDK
FROM openjdk:21-jdk-slim

# Copy the JAR file into the Docker image
ARG JAR_FILE=target/authors-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} authors-service.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/authors-service.jar"]

# Use a base image with OpenJDK
FROM openjdk:21-jdk-slim

# Copy the JAR file into the Docker image
ARG JAR_FILE=target/books-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} books-service.jar

# Expose the Books Service port
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/books-service.jar"]

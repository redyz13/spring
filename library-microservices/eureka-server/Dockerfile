# Use a base image with OpenJDK
FROM openjdk:21-jdk-slim

# Needed for health checks
RUN apt-get update && apt-get install -y curl

# Copy the JAR file into the Docker image
ARG JAR_FILE=target/eureka-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} eureka-server.jar

# Expose the Eureka Server port
EXPOSE 8761

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/eureka-server.jar"]

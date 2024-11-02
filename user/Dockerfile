# Use an OpenJDK image
FROM openjdk:21-jdk-slim

LABEL MAINTAINER=eng.sameh95@outlook.com

# Copy the Spring Boot jar to the container
COPY target/user-1.0.0.jar app.jar

# Document the port the app listens on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

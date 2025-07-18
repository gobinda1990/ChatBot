# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy WAR file to container
COPY target/ChatBot.war app.war
COPY lib/ojdbc6-11.2.0.4.jar /app/lib/ojdbc6-11.2.0.4.jar

# Expose the port your app runs on (usually 8080)
EXPOSE 8082

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.war"]

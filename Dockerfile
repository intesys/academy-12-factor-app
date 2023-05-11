# Use a base image with Java 17 installed
FROM adoptopenjdk:17-jdk-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/twelve-factor-demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar my-app.jar

# Expose the port your application listens on (if applicable)
EXPOSE 8080

# Set the command to run your application when the container starts
CMD ["java", "-jar", "my-app.jar"]

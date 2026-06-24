# Use an official Java runtime as a parent image
FROM amazoncorretto:21-alpine-jdk

# Set the working directory in the container
WORKDIR /app

# Create a non-root user and group
RUN addgroup -S oficina360 && adduser -S oficina360 -G oficina360

# Copy the JAR file into the container with ownership for the non-root user
COPY --chown=oficina360:oficina360 target/*.jar app.jar

# Create logs directory and adjust permissions
RUN mkdir -p /app/logs && chown -R oficina360:oficina360 /app

# Switch to non-root user
USER oficina360

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use an official JRE (not JDK) — smaller, and sufficient to run the app
FROM eclipse-temurin:17-jre

# Create a non-root user (security best practice)
RUN useradd -m appuser
USER appuser

WORKDIR /app

# Copy the built JAR
COPY target/assettracker-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
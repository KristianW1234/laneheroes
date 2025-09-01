# Use Java base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory in container
WORKDIR /app

# Copy Maven build output (the JAR)
COPY target/laneheroes-0.0.1-SNAPSHOT.jar app.jar

COPY img/noimage.png /app/img/noimage.png

# Expose port (match your Spring Boot server.port)
EXPOSE 8080

# Add init script
COPY docker-init.sh /docker-init.sh
RUN chmod +x /docker-init.sh

# Command to run the app
# ENTRYPOINT ["java","-jar","app.jar"]
ENTRYPOINT ["/docker-init.sh"]
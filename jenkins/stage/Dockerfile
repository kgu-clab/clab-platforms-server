# Use the official OpenJDK 21 image from the Docker Hub
FROM openjdk:21-jdk

# Expose port 8080 to the outside world
EXPOSE 8080

# Copy the JAR file into the container
COPY build/libs/clab.jar /clab.jar

# Set the default active profile to 'stage'. Modify the 'spring.profiles.active' property to match your environment.
# For example, use '-Dspring.profiles.active=production' for production environment.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=stage", "/clab.jar"]

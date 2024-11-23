# 1. Build Stage
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app

# Copy Gradle files and install dependencies
COPY build.gradle settings.gradle /app/
RUN gradle dependencies --stacktrace

# Copy source code and build
COPY src /app/src
RUN gradle bootJar --no-daemon --stacktrace

# Extract layers from JAR file
RUN java -Djarmode=layertools -jar build/libs/*.jar extract \
    && ls -l /app \
    && ls -l /app/dependencies \
    && ls -l /app/spring-boot-loader \
    && ls -l /app/snapshot-dependencies \
    && ls -l /app/application

# 2. Runtime Stage
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copy each layer
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=stage", "org.springframework.boot.loader.launch.JarLauncher"]

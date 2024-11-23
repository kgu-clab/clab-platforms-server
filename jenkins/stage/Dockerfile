# 1. 빌드 단계
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app

# Gradle 파일 복사 및 의존성 설치
COPY build.gradle settings.gradle /app/
RUN gradle build -x test --parallel --stacktrace || true

# 소스 코드 복사 및 빌드
COPY . /app
RUN gradle bootJar --no-daemon --stacktrace

# JAR 파일에서 레이어 추출
RUN java -Djarmode=layertools -jar build/libs/*.jar extract

# 2. 런타임 단계
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# 레이어별로 복사
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=stage", "org.springframework.boot.loader.JarLauncher"]

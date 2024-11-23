# 1. 빌드 단계
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app

# 의존성 캐싱을 위한 Gradle 파일 복사
COPY build.gradle settings.gradle /app/

# 의존성 설치 (자주 변경되지 않는 단계를 먼저 수행하여 캐싱 효과를 높임)
RUN gradle dependencies --parallel --stacktrace

# 소스 코드 복사 및 애플리케이션 빌드
COPY . /app
RUN gradle build -x test --parallel --stacktrace

# 2. 런타임 단계
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/clab.jar clab.jar

# 런타임 설정
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=stage", "clab.jar"]

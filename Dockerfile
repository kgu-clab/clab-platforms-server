FROM openjdk:21
EXPOSE 8080
COPY build/libs/clab-prod.jar /clab.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=stage", "/clab.jar"]

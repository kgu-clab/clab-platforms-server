FROM adoptopenjdk/openjdk21:alpine
ENV JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-arm64
ENV PATH=$JAVA_HOME/bin:$PATH
RUN $JAVA_HOME/bin/java -version

EXPOSE 8080
COPY build/libs/clab.jar /clab.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=stage", "/clab.jar"]

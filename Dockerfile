FROM jenkins/jenkins:2.440.3-lts-jdk21

USER root

RUN apt-get update && \
    apt-get install -y docker.io

USER jenkins

FROM ubuntu:latest
FROM openjdk:17-oracle
LABEL authors="MingXingWuJiang"
WORKDIR /app
COPY target/my-project-backend.jar /app/my-project-backend.jar
EXPOSE 1145

ENTRYPOINT ["java", "-jar", "my-project-backend.jar"]

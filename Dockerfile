FROM openjdk:21-jdk-slim

LABEL authors="muhammad.rayees"

WORKDIR /app

COPY target/crud-demo-docker-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:18-jdk-slim

WORKDIR /app

COPY target/api-gateway-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 
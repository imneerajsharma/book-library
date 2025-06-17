FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/book_library-0.0.1-SNAPSHOT.jar app.jar

# Optional: expose port for documentation, but not required for docker-compose
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]

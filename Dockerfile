# Runtime-Image mit Java 21
FROM eclipse-temurin:21-jre-jammy

# Erstelle Verzeichnis für die App
WORKDIR /app

RUN echo "Folgendes liegt in /build/libs/:" && ls -lh /build/libs/

# Kopiere das fertige ShadowJar ins Image
COPY build/libs/app.jar app.jar

RUN echo "Folgendes liegt in /app:" && ls -lh /app

# Setze den Container-Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]
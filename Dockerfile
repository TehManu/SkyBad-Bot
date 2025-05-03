# Runtime-Image mit Java 21
FROM eclipse-temurin:21-jre-jammy

# Installiere bash, falls benötigt
RUN apt-get update && apt-get install -y bash

# Erstelle Verzeichnis für die App
WORKDIR /app

# Kopiere das fertige ShadowJar ins Image
COPY build/libs/*.jar app.jar

# Setze den Container-Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]
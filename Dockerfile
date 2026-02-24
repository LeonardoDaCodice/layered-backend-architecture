# Usa immagine ufficiale Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directory di lavoro
WORKDIR /app

# Copia il jar generato
COPY target/layered-backend-architecture-0.0.1-SNAPSHOT.jar app.jar

# Espone porta 8080
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
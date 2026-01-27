# Imagen base con Java 17
FROM eclipse-temurin:17-jre

# Directorio de trabajo
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/temperature.jar app.jar

# Render expone el puerto por variable PORT
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]

# ---------- build stage ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copiamos lo necesario primero para aprovechar cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Bajamos dependencias (cache)
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copiamos el código y compilamos
COPY src src
RUN ./mvnw -DskipTests package

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado (tomamos el único jar de target)
COPY --from=build /workspace/target/*.jar app.jar

# Render usa PORT
EXPOSE 8080

# Importante: Spring Boot leerá server.port=${PORT:8080}
ENTRYPOINT ["java","-jar","/app/app.jar"]

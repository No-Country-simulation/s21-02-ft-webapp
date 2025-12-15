# Etapa 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia el pom.xml y descarga dependencias (caching)
COPY pom.xml .
RUN apt-get update -qq && apt-get install -y -qq maven && mvn dependency:go-offline

# Copia el código fuente
COPY src ./src

# Compila el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el jar desde el build stage
COPY --from=build /app/target/*.jar app.jar

# Comando para ejecutar la app
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
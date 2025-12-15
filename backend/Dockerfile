# Etapa 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 1. Copia archivos necesarios
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

# 2. Descarga dependencias
RUN ./mvnw dependency:go-offline -B

# 3. Copia código
COPY src ./src

# 4. COMPILA sin tests (SKIP completo)
RUN ./mvnw clean compile -DskipTests

# 5. CREA JAR sin tests
RUN ./mvnw package -DskipTests -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el jar
COPY --from=build /app/target/*.jar app.jar

# Puerto de Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
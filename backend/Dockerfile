# ============================================
# DOCKERFILE OPTIMIZADO Y SIMPLE
# ============================================

# ---- ETAPA 1: Compilación ----
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# Configurar zona horaria (IMPORTANTE para fechas)
ENV TZ=America/Argentina/Buenos_Aires

# Crear directorio de trabajo
WORKDIR /app

# 1. Copiar archivo de dependencias primero (optimización cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copiar código fuente
COPY src ./src

# 3. Descargar dependencias y compilar
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# ---- ETAPA 2: Ejecución ----
FROM eclipse-temurin:21-jre-alpine

# Configurar zona horaria en runtime también
RUN apk add --no-cache tzdata
ENV TZ=America/Argentina/Buenos_Aires

# Directorio de trabajo
WORKDIR /app

# Copiar JAR generado
COPY --from=builder /app/target/*.jar app.jar

# Puerto (8080 para local, Render usa 10000)
EXPOSE 8080

# HEALTHCHECK: Verifica cada 30s si la app responde
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
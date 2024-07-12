# Etapa 1: Construcción
FROM maven:3.8.4-openjdk-17-slim AS build

# Establecer el directorio de trabajo en el contenedor
WORKDIR /

# Copiar el archivo pom.xml y descargar las dependencias del proyecto
COPY pom.xml .

# Descarga de las dependencias necesarias
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente del proyecto
COPY src ./src

# Construir el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo en el contenedor
WORKDIR /

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /target/instrumentos-0.0.1-SNAPSHOT.jar /instrumentos-0.0.1-SNAPSHOT.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# El comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "instrumentos-0.0.1-SNAPSHOT.jar"]

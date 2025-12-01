# Multi-stage Dockerfile: build with Maven wrapper and run with Eclipse Temurin JRE 21
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only the files needed for a build to leverage Docker cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw && ./mvnw -B -DskipTests clean package

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built jar (assumes single jar in target produced by Maven)
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

# Render provides $PORT at runtime; use shell form to allow env expansion
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar /app/app.jar"]


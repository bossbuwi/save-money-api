# Stage 1: Build the JAR
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21
ARG PORT
ENV PORT=$PORT
RUN mkdir -p /opt/app
COPY --from=builder /app/target/*.jar /opt/app/app.jar
EXPOSE $PORT
CMD ["java", "-jar", "/opt/app/app.jar"]
# Stage 1: Build the JAR
FROM maven:3.8.6-openjdk-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21
ARG SUPABASE_DB_URL
ARG SUPABASE_API_KEY
ARG AUTH_URL
ARG AUTH_API_KEY
ARG PORT
ENV SUPABASE_DB_URL=$SUPABASE_DB_URL
ENV SUPABASE_API_KEY=$SUPABASE_API_KEY
ENV AUTH_URL=$AUTH_URL
ENV AUTH_API_KEY=$AUTH_API_KEY
ENV PORT=$PORT
RUN mkdir /opt/app
COPY --from=builder /app/target/*.jar /opt/app/app.jar
EXPOSE $PORT
CMD ["java", "-jar", "/opt/app/app.jar"]
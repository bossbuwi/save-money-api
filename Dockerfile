FROM eclipse-temurin:21
RUN mkdir /opt/app
# Copy from CI artifact location
COPY target/*.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
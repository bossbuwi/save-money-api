FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/save-money-0.0.2-PRE.jar /opt/app
CMD ["java", "-jar", "/opt/app/save-money-0.0.2-PRE.jar"]
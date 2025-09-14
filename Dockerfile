FROM eclipse-temurin:21
# Receive build arguments
ARG SUPABASE_DB_URL
ARG SUPABASE_API_KEY
# Set the arguments as environment variables
ENV SUPABASE_DB_URL=$SUPABASE_DB_URL
ENV SUPABASE_API_KEY=$SUPABASE_API_KEY
# Create the directory where the package will be stored
RUN mkdir /opt/app
# Copy from CI artifact location
COPY target/*.jar /opt/app/app.jar
EXPOSE $PORT
CMD ["java", "-jar", "/opt/app/app.jar"]
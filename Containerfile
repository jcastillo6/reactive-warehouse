# Stage 1: Build the application
FROM amazoncorretto:17 AS build
WORKDIR /app

# Copy only the necessary files needed for dependency resolution
COPY build.gradle settings.gradle /app/

# Copy the entire project
COPY . /app/

# Build the application with Gradle
RUN ./gradlew build

# Stage 2: Create a smaller image for the runtime
FROM amazoncorretto:17
WORKDIR /app

# Copy only the JAR file from the build stage
COPY --from=build /app/build/libs/reactive-warehouse-0.0.1-SNAPSHOT.jar /app/

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "reactive-warehouse-1.0.0.jar"]
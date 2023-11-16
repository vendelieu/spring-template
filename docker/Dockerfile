# First stage: build the application
FROM gradle:8.4-jdk17-alpine AS build
COPY . /app
WORKDIR /app
RUN gradle build

# Second stage: create a slim image
FROM openjdk:11.0.16-jre-slim
COPY --from=build /app/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
# First stage: build the application
FROM gradle:8.6-jdk17-alpine AS build
COPY . /app
WORKDIR /app
RUN gradle build

# Second stage: create a slim image
FROM openjdk:21-jdk
RUN mkdir /app
# RUN microdnf install findutils
COPY build/libs/app.jar /app/
WORKDIR /app/
CMD ["java", "-jar" , "app.jar"]
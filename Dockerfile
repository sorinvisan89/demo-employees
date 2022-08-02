# Maven base image
FROM maven:3.8.2-openjdk-11 AS builder

WORKDIR /app

# Copy original pom.xml
COPY pom.xml /app/pom.xml

# Fetch all dependencies and cache them
RUN mvn --batch-mode dependency:go-offline

# Copy sources
COPY src /app/src

# Build the jar.
# We skip running tests and spotless when building in this dockerfile. There is no point in running them here, they are run by the mvn clean install goals.
# Also, spotless passes the checks in mvn clean install, but fails checks when running inside the Dockerfile on Windows environments.
# It passed the mvn clean install lifecycle, but fails since the files have Windows line endings and they are copied inside an UNIX distribution when building the image.
RUN mvn --batch-mode install -DskipTests -DskipIT -Dspotless.check.skip=true

#  OpenJDK base image
FROM openjdk:11.0.15-slim

RUN mkdir -p /app
# Copy from first step the jar

COPY --from=builder /app/target/*.jar /app/employees.jar
WORKDIR /app

ENV DOCKER_ACTIVE_MQ_CONTAINER=activemq-demo
ENV DOCKER_DB_CONTAINER=postgres-demo

ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=*:8002,server=y,suspend=n

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=local", "-jar", "/app/employees.jar"]
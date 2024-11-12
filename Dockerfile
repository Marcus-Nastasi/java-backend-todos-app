# build phase
FROM maven:3.9-eclipse-temurin-21 AS build

# Define workdir
WORKDIR /app

# Copy pom.xml file and dependencies to cache
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source-code to workdir
COPY src ./src

# Project copile and create final JAR
RUN mvn clean package -DskipTests

# Final phase
FROM openjdk:21-oracle

# Define workdir
WORKDIR /app

# Copy the generated JAR to workdir
COPY --from=build /app/target/todos-0.0.1-SNAPSHOT.jar /app/todos-0.0.1-SNAPSHOT.jar

# Expose port
EXPOSE 8080

# Define entry point command
ENTRYPOINT ["java", "-jar", "/app/todos-0.0.1-SNAPSHOT.jar"]

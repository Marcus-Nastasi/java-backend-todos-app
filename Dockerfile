FROM openjdk:21-oracle
WORKDIR /app
COPY todos-0.0.1-SNAPSHOT.jar /app/todos-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/todos-0.0.1-SNAPSHOT.jar"]




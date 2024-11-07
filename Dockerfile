# Etapa de build
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e as dependências para o cache
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copia o código-fonte do projeto para o diretório de trabalho
COPY src ./src

# Compila o projeto e cria o JAR final
RUN mvn clean package -DskipTests

# Etapa final
FROM openjdk:21-oracle

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado na etapa de build para o diretório de trabalho
COPY --from=build /app/target/todos-0.0.1-SNAPSHOT.jar /app/todos-0.0.1-SNAPSHOT.jar

# Expõe a porta
EXPOSE 8080

# Define o comando de entrada
ENTRYPOINT ["java", "-jar", "/app/todos-0.0.1-SNAPSHOT.jar"]

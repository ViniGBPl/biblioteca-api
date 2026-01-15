# Estágio 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml para baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e gera o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Execução com uma imagem leve do JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
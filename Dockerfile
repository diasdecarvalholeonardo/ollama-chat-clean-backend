# =============================
# 1. ESTÁGIO DE BUILD (COMPILAÇÃO)
# Imagem: Maven para compilar o JAR
# =============================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia e baixa dependências primeiro (uso eficiente de cache do Docker)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Compila o projeto (cria o JAR)
RUN mvn -B clean package -DskipTests

# =============================
# 2. ESTÁGIO DE EXECUÇÃO FINAL (RUNTIME)
# Imagem: Apenas o JRE (Java Runtime Environment) - Leve
# =============================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR compilado do estágio 'build' para o estágio final
COPY --from=build /app/target/*.jar app.jar

# Define a porta exposta
EXPOSE 8080

# Comando de execução do Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
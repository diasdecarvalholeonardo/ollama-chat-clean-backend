# =============================
# 1. ESTÁGIO DE BUILD (COMPILAÇÃO)
# =============================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Garante explicitamente o Java 21
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

# Copia apenas o pom para cache eficiente
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Verificação explícita do Java usado no build
RUN java -version && javac -version

# Compila o projeto
RUN mvn -B clean package -DskipTests


# =============================
# 2. ESTÁGIO DE RUNTIME (EXECUÇÃO)
# =============================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR final
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

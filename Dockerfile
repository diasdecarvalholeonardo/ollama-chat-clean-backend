# =============================
# 1) FRONTEND BUILD (SUPER OTIMIZADO)
# =============================
FROM node:20 AS frontend-builder
WORKDIR /app/frontend

# Copiar apenas os arquivos de dependências primeiro
COPY frontend/package.json frontend/package-lock.json ./

# Instalar dependências com cache
RUN npm install --legacy-peer-deps

# Copiar o restante do frontend
COPY frontend/ .

# Build de produção
RUN npm run build



# =============================
# 2) BACKEND BUILD (SPRING)
# =============================
FROM maven:3.9.6-eclipse-temurin-22 AS backend-builder
WORKDIR /app

# Copiar POM primeiro (cache eficiente)
COPY pom.xml .

# Baixar dependências do Maven (fica em cache)
RUN mvn -B -q dependency:go-offline

# Copiar código-fonte
COPY src ./src

# --- INTEGRA NOVA FUNCIONALIDADE SEM QUEBRAR SUA LÓGICA ---
# Copiar o build do frontend para dentro do backend ANTES do package
# Isso coloca o frontend dentro do JAR (Spring Boot static/)
COPY --from=frontend-builder /app/frontend/dist ./src/main/resources/static

# Build do backend
RUN mvn -B -q clean package -DskipTests



# =============================
# 3) FINAL IMAGE (ULTRA LEVE)
# =============================
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app

# Copiar JAR final
COPY --from=backend-builder /app/target/*.jar app.jar

# (Opcional) Se quiser manter static externo:
# COPY --from=frontend-builder /app/frontend/dist ./static/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

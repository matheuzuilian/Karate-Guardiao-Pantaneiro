# --- ETAPA 1: Construir o projeto (.jar) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Gera o jar pulando os testes para ser mais rápido
RUN mvn clean package -DskipTests

# --- ETAPA 2: Rodar a aplicação ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o jar gerado na etapa anterior para o container atual
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta em que o Spring Boot roda (ex: 8080 ou 8182)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
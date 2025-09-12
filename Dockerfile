# Estágio 1: Build - Usamos uma imagem com JDK e Gradle para compilar o projeto
FROM gradle:jdk21 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia todo o código do projeto para o container
COPY . .

# Comando para compilar o projeto e gerar o JAR executável
# --no-daemon é recomendado para ambientes de CI/CD e Docker
RUN gradle bootJar --no-daemon

# Estágio 2: Run - Usamos uma imagem JRE mínima para rodar a aplicação
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o JAR gerado no estágio anterior para a imagem final
COPY --from=builder /app/build/libs/*.jar app.jar

# Expõe a porta da aplicação e a porta de DEBUG
EXPOSE 8080
EXPOSE 5005

# Comando para iniciar a aplicação com os argumentos de debug
# suspend=n: a JVM não espera o debugger se conectar para iniciar
# address=*:5005: o debugger vai "ouvir" em todas as interfaces na porta 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
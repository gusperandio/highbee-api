# Use uma imagem que já tenha o JDK 21
FROM gradle:jdk21

# Define o diretório de trabalho dentro do container
WORKDIR /app

# NOTA: Nenhuma instrução COPY aqui, pois o código será
# inteiramente montado pelo docker-compose.yml.

# Expõe a porta da aplicação e a porta de DEBUG
EXPOSE 8080
EXPOSE 5005

# O comando para iniciar a aplicação será fornecido pelo docker-compose.yml
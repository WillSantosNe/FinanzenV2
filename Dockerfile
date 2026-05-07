
# Sistema base
FROM eclipse-temurin:21-jdk-alpine

# Pasta de trabalho
WORKDIR /app

# Copiando jar da máquina para o contêiner
COPY target/api-0.0.1-SNAPSHOT.jar api.jar

# Porta que o spring usa
EXPOSE 8080

# O que o contêiner vai rodar quando ligar
ENTRYPOINT ["java", "-jar", "api.jar"]

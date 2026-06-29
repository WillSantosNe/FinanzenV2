
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# limita o heap do Java em 350MB para não estourar os 512MB da Render
ENTRYPOINT ["java", "-Xmx350m", "-XX:+UseSerialGC", "-jar", "app.jar"]
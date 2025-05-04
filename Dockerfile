# Dockerfile na raiz do projeto
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]

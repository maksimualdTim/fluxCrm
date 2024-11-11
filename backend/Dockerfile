FROM eclipse-temurin:21-jdk-noble

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN ./mvnw dependency:go-offline

CMD ["./mvnw", "spring-boot:run"]
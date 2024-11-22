FROM eclipse-temurin:21-jre-noble

WORKDIR /app

EXPOSE 8080

COPY target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
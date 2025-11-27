FROM eclipse-temurin:23-jre

WORKDIR /app
COPY /target/SpringBootWebOnlineStoreShowcase-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["./app.jar"]

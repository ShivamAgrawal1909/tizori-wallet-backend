FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

RUN apk add --no-cache ca-certificates && update-ca-certificates

COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

RUN chmod +x mvnw && ./mvnw package -DskipTests

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/ewallet-0.0.1-SNAPSHOT.jar"]
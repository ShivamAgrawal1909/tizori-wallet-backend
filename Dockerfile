FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

RUN apk add --no-cache ca-certificates openssl && update-ca-certificates

COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

RUN chmod +x mvnw && ./mvnw package -DskipTests

COPY start.sh .
RUN chmod +x start.sh
RUN cp target/ewallet-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["./start.sh"]
# Step 1 - Java 21 base image use karo
FROM eclipse-temurin:21-jdk-alpine

# Step 2 - Working directory set karo
WORKDIR /app

# Step 3 - pom.xml aur source code copy karo
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn

# Step 4 - Project build karo
RUN ./mvnw package -DskipTests

# Step 5 - JAR file run karo
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/ewallet-0.0.1-SNAPSHOT.jar"]
# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first for caching
COPY examTracker/pom.xml ./pom.xml
COPY examTracker/.mvn ./.mvn
COPY examTracker/mvnw ./mvnw

# Download dependencies
RUN mvn -B -q dependency:go-offline

# Copy source code
COPY examTracker/src ./src

# Build jar
RUN mvn clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

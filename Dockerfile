# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml first (better Docker cache)
COPY examTracker/pom.xml ./pom.xml
RUN mvn -B -q dependency:go-offline

# Copy source
COPY examTracker/src ./src

# Build the jar
RUN mvn clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

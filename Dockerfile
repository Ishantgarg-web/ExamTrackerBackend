# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom first (dependency cache)
COPY examTracker/pom.xml ./pom.xml
COPY examTracker/.mvn ./.mvn
COPY examTracker/mvnw ./mvnw

RUN mvn -B dependency:go-offline

# Copy source
COPY examTracker/src ./src

# Build
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

# All configs come from ENV
ENTRYPOINT ["java", "-jar", "app.jar"]

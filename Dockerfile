# ----- MULTI STAGE DOCKER BUILD -----

# BUILD STAGE
FROM maven:3.9-eclipse-temurin-21 AS build

LABEL author='akash'

WORKDIR /app

# Copy maven configuration first
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy application source and tests
COPY src ./src
COPY tests ./tests

# Build the spring boot JAR
RUN mvn clean package -DskipTests


# RUNTIME STAGE
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the build JAR from the "build stage"
COPY --from=build /app/target/*.jar app.jar

# PORT
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
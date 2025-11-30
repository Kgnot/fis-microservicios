# ---------------------------
# BUILD STAGE
# ---------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build optimizado con cache + bajo uso de RAM
RUN --mount=type=cache,target=/root/.m2 \
    MAVEN_OPTS="-Xmx256m -XX:MaxMetaspaceSize=128m" \
    mvn -T 1C clean package -DskipTests

# ---------------------------
# RUNTIME STAGE
# ---------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/usuario-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8093
ENTRYPOINT ["java", "-jar", "app.jar"]

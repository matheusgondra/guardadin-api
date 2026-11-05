FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src/ src/

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

RUN useradd -u 1001 spring && chown -R spring:spring /app
USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
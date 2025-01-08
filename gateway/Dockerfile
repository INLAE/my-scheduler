# -------------------- (1) ЭТАП СБОРКИ --------------------
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# тянем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# копируем исходники
COPY src ./src
RUN mvn package -DskipTests

# -------------------- (2) ЭТАП РАНТАЙМ --------------------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
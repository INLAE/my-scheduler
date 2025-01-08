# -------------------- (1) ЭТАП СБОРКИ --------------------
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Сначала копируем pom.xml (чтобы подтянуть зависимости)
COPY pom.xml .
RUN mvn dependency:go-offline

# Потом копируем исходники и собираем
COPY src ./src
RUN mvn package -DskipTests

# -------------------- (2) ЭТАП РАНТАЙМ --------------------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Копируем итоговый jar из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Если у вас jar может называться иначе, проверяйте имя. Можно указать конкретно:
# COPY --from=build /app/target/domain-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
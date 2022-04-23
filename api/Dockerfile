FROM openjdk:17-jdk-slim AS build
WORKDIR /app

COPY . .
RUN ./gradlew build --no-daemon

FROM openjdk:17-slim AS release
WORKDIR /app

COPY --from=build /app/build/libs/api-*.jar /app/api.jar
CMD ["java", "-jar", "/app/api.jar"]

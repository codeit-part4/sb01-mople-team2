FROM amazoncorretto:17-alpine AS builder

WORKDIR /app
COPY . .

RUN ./gradlew :sse:bootJar --no-daemon

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/sse/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

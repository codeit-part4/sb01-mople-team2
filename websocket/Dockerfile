FROM amazoncorretto:17-alpine AS builder

WORKDIR /app
COPY . .

RUN ./gradlew :websocket:bootJar --no-daemon

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/websocket/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

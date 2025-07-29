FROM amazoncorretto:17-alpine AS builder

WORKDIR /app

# 빌드 설정 파일만 먼저 복사 (캐시 최적화)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 소스 코드 복사
COPY src ./src

# gradlew 실행 가능하도록
RUN chmod +x ./gradlew

# 빌드
RUN ./gradlew clean bootJar --no-daemon

RUN cp build/libs/*-SNAPSHOT.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

COPY src/main/resources/application-prod.yaml ./application-prod.yaml

ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

ENTRYPOINT ["sh", "-c","java $JAVA_OPTS -Duser.timezone=Asia/Seoul -Dserver.port=$SERVER_PORT -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE org.springframework.boot.loader.launch.JarLauncher"]

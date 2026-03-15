FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

ENV TZ=UTC \
    SERVER_PORT=8080 \
    ENABLE_DEBUG=false \
    JAVA_OPTS="" \
    JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
    SENTINEL_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/sentinel_core \
    SENTINEL_DATASOURCE_USERNAME=sentinel \
    SENTINEL_DATASOURCE_PASSWORD=sentinel \
    SENTINEL_AGENT_API_KEY=change-me-agent-key \
    SENTINEL_ADMIN_USERNAME=admin \
    SENTINEL_ADMIN_PASSWORD=change-me-admin-password

COPY --from=builder /workspace/target/*.jar /app/app.jar

EXPOSE 8080 5005

ENTRYPOINT ["/bin/sh", "-c", "if [ \"$ENABLE_DEBUG\" = \"true\" ]; then exec java $JAVA_OPTS $JAVA_DEBUG_OPTS -jar /app/app.jar; else exec java $JAVA_OPTS -jar /app/app.jar; fi"]
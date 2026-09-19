FROM maven:4.0.0-rc-5-eclipse-temurin-25
WORKDIR /app

COPY pom.xml .

COPY src ./src
RUN mvn clean package -DskipTests

EXPOSE 8080

# JAVA_TOOL_OPTIONS JVM подхватывает сама, в отличие от JAVA_OPTS
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["java", "-jar", "./target/lab1-4.0.0.jar", "--spring.profiles.active=docker"]
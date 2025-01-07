FROM maven:3.9.9-amazoncorretto-21 AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src
RUN mvn package -DskipTests

FROM openjdk:21-jdk-slim
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/docker-spring-todo/app.jar
ENTRYPOINT ["java", "-jar", "/opt/docker-spring-todo/app.jar"]
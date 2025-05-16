FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} ferremas-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/ferremas-backend.jar"]
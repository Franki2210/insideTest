FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY db/db.sqlite db/db.sqlite
ENTRYPOINT ["java","-jar","/app.jar"]
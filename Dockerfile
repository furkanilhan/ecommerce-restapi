# Base image
FROM openjdk:17-jdk-slim

# Application jar file
ARG JAR_FILE=target/ecommerce-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Application port
EXPOSE 8080

# run Application
ENTRYPOINT ["java","-jar","/app.jar"]

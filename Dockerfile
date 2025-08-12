# generate the jar
FROM eclipse-temurin:17-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY ./build/libs/*.jar /app/app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

FROM openjdk:8-jdk-alpine
ADD ./build/libs/statement-loader-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
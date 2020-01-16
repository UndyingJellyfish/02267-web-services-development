FROM adoptopenjdk:8-jre-hotspot
WORKDIR /usr/src
EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /usr/src/app.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app.jar"]
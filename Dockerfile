FROM openjdk:8-jre-alpine
WORKDIR /usr/src/app
COPY target/pricealarm*.jar .
EXPOSE 8080
CMD ["java", "-jar", "pricealarm-0.0.1-SNAPSHOT.jar"]

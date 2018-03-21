FROM openjdk:8

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/consumer-1.0-SNAPSHOT.jar $PROJECT_HOME/consumer.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME

ENTRYPOINT ["java", "-jar", "-Dspring.data.mongodb.uri=mongodb://mongo/test", "./consumer.jar"]
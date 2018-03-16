FROM openjdk:8

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/producer-1.0-SNAPSHOT.jar $PROJECT_HOME/producer.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME

ENTRYPOINT ["java", "-jar", "./producer.jar"]
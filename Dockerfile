FROM openjdk:8

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/kafka-producer-1.0-SNAPSHOT.jar $PROJECT_HOME/kafka-producer.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME

#ENTRYPOINT ["java", "-jar", "mongo-docker.jar"]
ENTRYPOINT ["java", "-jar", "-Dspring.data.mongodb.uri=mongodb://mongo/test", "./kafka-producer.jar"]
FROM openjdk:8

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

ENV KAFKA_PORT 9092
ENV KAFKA_PRODUCER_HOST 'kafka'
ENV KAFKA_PRODUCER_URL $KAFKA_PRODUCER_HOST':'$KAFKA_PORT

ENV JAVA_OPTS '-Dspring.data.mongodb.uri=mongodb://mongo/test -Dspring.kafka.producer.bootstrap-servers='$KAFKA_PRODUCER_URL

COPY target/producer-1.0-SNAPSHOT.jar $PROJECT_HOME/producer.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME

ENTRYPOINT exec java $JAVA_OPTS  -jar ./producer.jar
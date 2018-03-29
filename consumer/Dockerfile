FROM openjdk:8

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

ENV KAFKA_PORT 9092
ENV KAFKA_CONSUMER_HOST 'kafka'
ENV KAFKA_CONSUMER_URL $KAFKA_CONSUMER_HOST':'$KAFKA_PORT

ENV JAVA_OPTS '-Dspring.data.mongodb.uri=mongodb://mongo/test -Dspring.kafka.consumer.bootstrap-servers='$KAFKA_CONSUMER_URL

COPY target/consumer-1.0-SNAPSHOT.jar $PROJECT_HOME/consumer.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME

ENTRYPOINT exec java $JAVA_OPTS  -jar ./consumer.jar
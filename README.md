# kafkaproducer
Dockerised Kafka Container and REST service to access topic messages

# To run containers from a PC

* Checkout project and change directory to the project
* To run ```docker-compose up```
* To view collections in mongoDb running in docker, ```docker exec -it mongo1 mongo```
* To shutdown ```docker-compose down```


# Docker container images

The docker-compose.yml file builds and deploys the following containers:

* restfulservice (Spring Boot REST endpoints)
* Kafka
* MongoDb
* Zookeeper

## restfulservice

### REST endpoint for posting an Employee JSON payload into a Kafka Topic

> URL: http://localhost:8081/employee (POST)
> Body Exmaple:
```
{
   "email" : "simon.jones@gmail.com",
   "fullName" : "Simon Jones",
   "managerEmail" : "simons.boss@gmail.com"
}
```

### REST Endpoint for getting all employees from a Mongo store;
URL: http://localhost:8081/employee (GET)

### REST Endpoint for quering an employee from a Mongo store;
URL: http://localhost:8080/message/{email}/ (GET)

## Kafka (topic: "acxTopic")

The POST REST endpoint will act as a Kafka producer to put Employee messages on the topic.
Topic consumer listeners will receive Employee messages and store the record in Mongo

## Zookeeper

Required by Kafka for orchestration.


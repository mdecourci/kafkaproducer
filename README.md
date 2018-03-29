# kafkaproducer
Dockerised Kafka Container and REST service to access topic messages

# To run containers in a local deployment

* Checkout project and change directory to the project
* To run ```docker-compose -f docker-compose-local.yml up```
* To view collections in mongoDb running in docker, ```docker exec -it mongo1 mongo```
* To shutdown ```docker-compose -f docker-compose-local.yml down```

# To run containers in a AWS ECS deployment

* Checkout project and change directory to the project
* Ensure there is a ```docker-compose.yml```  (the AWS file used by ```ecs-cli```)
* Ensure ```ecs-cli``` is installed and configured for a AWS ECS cloud environment
* To run ```ecs-cli compose --debug service up -create-log-groups```
* To view collections in mongoDb, post data as below and get data using the consumer GET endpoint
* To shutdown ```ecs-cli compose --debug service down```

# Docker container images

The docker-compose.yml file builds and deploys the following containers:

* producer (Spring Boot POST REST endpoints)
* consumer (Spring Boot GET REST endpoints)
* Kafka
* MongoDb
* Zookeeper

## producer

### REST endpoint for posting an Employee JSON payload into a Kafka Topic

> URL: http://localhost:8081/producer/employee (POST)
> Body Exmaple:
```
{
   "email" : "simon.jones@gmail.com",
   "fullName" : "Simon Jones",
   "managerEmail" : "simons.boss@gmail.com"
}
```
> The hostname for the URL will be different in AWS EC2
## consumer

### REST Endpoint for getting all employees from a Mongo store;
URL: http://localhost:8081/consumer/employee (GET)

### REST Endpoint for quering an employee from a Mongo store;
URL: http://localhost:8081/consumer/message/{email}/ (GET)
> The hostname for the URL will be different in AWS EC2

## Kafka (topic: "acxTopic")

The POST REST endpoint will act as a Kafka producer to put Employee messages on the topic.
Topic consumer listeners will receive Employee messages and store the record in Mongo

## Zookeeper

Required by Kafka for orchestration.


package com.netpod.kafkdocker.services;

import com.netpod.kafkdocker.documents.Employee;
import com.netpod.kafkdocker.repository.EmployeeRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@Component
public class MongoKafkaConsumer implements KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoKafkaConsumer.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private KafkaCountDownLatch kafkaCountDownLatch;

    @KafkaListener(id = "mongoContainer", topics = "${acx.kafka.topic}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        LOGGER.info("received data='{}'", consumerRecord.toString());
        Employee employee = Employee.builder().message((String) consumerRecord.value()).build();
        if (!employee.isEmpty()) {
            employeeRepository.save(employee);
        }
        kafkaCountDownLatch.countDown();
    }

    @Override
    public Employee findByEmail(final String pEmail) {
        LOGGER.info("findByEmail pEmail='{}'", pEmail);
        return employeeRepository.findByEmail(pEmail);
    }

    @Override
    public List<Employee> findAll() {
        LOGGER.info("findAll");
        return employeeRepository.findAll();
    }
}

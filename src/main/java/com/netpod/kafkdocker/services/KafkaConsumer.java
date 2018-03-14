package com.netpod.kafkdocker.services;

import com.netpod.kafkdocker.documents.Employee;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface KafkaConsumer {

    void receive(ConsumerRecord<?, ?> consumerRecord);

    Employee findByEmail(String pEmail);

    List<Employee> findAll();
}

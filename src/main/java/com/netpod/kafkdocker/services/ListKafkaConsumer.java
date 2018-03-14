package com.netpod.kafkdocker.services;

import com.netpod.kafkdocker.documents.Employee;
import com.netpod.kafkdocker.repository.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Component
public class ListKafkaConsumer implements KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListKafkaConsumer.class);

    @Autowired
    private KafkaCountDownLatch kafkaCountDownLatch;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @KafkaListener(id = "messageContainer", topics = "${acx.kafka.topic}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        LOGGER.info("received data='{}'", consumerRecord.toString());
        messageRepository.save((String) consumerRecord.value());
        kafkaCountDownLatch.countDown();
    }

    @Override
    public Employee findByEmail(final String pEmail) {
        LOGGER.info("findByEmail pEmail = {}", pEmail);
        String message =  messageRepository.findByEmail(pEmail);
        return Employee.builder().message(message).build();
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        List<String> messages = messageRepository.findAll();

        if (!CollectionUtils.isEmpty(messages)) {
            employees = messages.stream().map(Employee.builder()::message).map(Employee.Builder::build).collect(Collectors.toList());
        }
        return employees;
    }

}

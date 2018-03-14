package com.netpod.kafkdocker.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

//@Component
public class Listener {

    @Autowired
    @Qualifier("mongoKafkaConsumer")
    private KafkaConsumer mongoKafkaConsumer;

    @Autowired
    @Qualifier("listKafkaConsumer")
    private KafkaConsumer listKafkaConsumer;

    @KafkaListener(id = "mongoContainer", topicPartitions = {@TopicPartition(topic = "${acx.kafka.topic}", partitions = {"0"})})
    public void listenPartition0(ConsumerRecord<?, ?> record) {
        System.out.println("Listener Id0, Thread ID: " + Thread.currentThread().getId());
        System.out.println("Received: " + record);
        mongoKafkaConsumer.receive(record);
    }

    @KafkaListener(id = "messageContainer", topicPartitions = {@TopicPartition(topic = "${acx.kafka.topic}", partitions = {"1"})})
    public void listenPartition1(ConsumerRecord<?, ?> record) {
        System.out.println("Listener Id1, Thread ID: " + Thread.currentThread().getId());
        System.out.println("Received: " + record);
        listKafkaConsumer.receive(record);
    }
}

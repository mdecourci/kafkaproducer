package com.netpod.kafkdocker.repository;

import com.netpod.kafkdocker.documents.Employee;
import com.netpod.kafkdocker.services.ListKafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);

    private CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();

    public void save(String msg) {
        LOGGER.info("save msg ='{}'", msg);
        messages.add(msg);
    }

    public String findByEmail(String pEmail) {
        String message =  messages.stream().filter(s -> s.contains(pEmail)).findFirst().get();
        LOGGER.info("Found message ='{}'", message);
        return message;
    }

    public List<String> findAll() {
        List<String> msgs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messages)) {
            msgs = messages.stream().collect(Collectors.toList());
        }
        return msgs;
    }

    public void deleteAll() {
        if (messages != null) {
            messages.clear();
        }
    }
}

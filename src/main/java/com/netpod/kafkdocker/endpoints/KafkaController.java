package com.netpod.kafkdocker.endpoints;


import com.netpod.kafkdocker.documents.Employee;
import com.netpod.kafkdocker.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ConfigurationProperties
@RestController
@RequestMapping("/")
public class KafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);

    @Value("${application.message}")
    private String message;

    @Value("${application.appname}")
    private String appname;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    @Qualifier("mongoKafkaConsumer")
    private KafkaConsumer mongoKafkaConsumer;

    @Autowired
    @Qualifier("listKafkaConsumer")
    private KafkaConsumer listKafkaConsumer;

    @Autowired
    private KafkaCountDownLatch kafkaCountDownLatch;

    @RequestMapping
    String home() {
        return "Hello World....- " + message + " " + appname;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public Employee create(@RequestBody Employee employee) {
        StringBuilder msg = new StringBuilder();
        msg.append("fullName=").append(employee.getFullName()).append(",");
        msg.append("email=").append(employee.getEmail()).append(",");
        msg.append("managerEmail=").append(employee.getManagerEmail());
        kafkaProducer.send(msg.toString());

        try {
            kafkaCountDownLatch.getLatch().await(10000, TimeUnit.MILLISECONDS);
            LOGGER.info("CountDown latch = {}", kafkaCountDownLatch.getLatch().getCount());
        } catch (InterruptedException e) {
            LOGGER.error("Unexpected error", e);
        }
        return employee;
    }

    @RequestMapping(value = "/employee/{email}/", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable(name = "email") String email) {
        Employee  employee =  mongoKafkaConsumer.findByEmail(email);
        LOGGER.info("Found employee = {}", employee);
        return employee;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public List<Employee> getAllEmployees() {
        return mongoKafkaConsumer.findAll();
    }

    @RequestMapping(value = "/message/{email}/", method = RequestMethod.GET)
    public Employee getMessage(@PathVariable(name = "email") String email) {
        Employee  employee =  listKafkaConsumer.findByEmail(email);
        LOGGER.info("Found employee = {}", employee);
        return employee;
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Employee> getAll() {
        return listKafkaConsumer.findAll();
    }

}

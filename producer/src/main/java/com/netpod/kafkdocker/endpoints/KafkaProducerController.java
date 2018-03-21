package com.netpod.kafkdocker.endpoints;


import com.netpod.kafkdocker.dto.EmployeeDto;
import com.netpod.kafkdocker.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties
@RestController
@RequestMapping("/producer")
public class KafkaProducerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerController.class);

    @Value("${application.message}")
    private String message;

    @Value("${application.appname}")
    private String appname;

    @Autowired
    private KafkaProducer kafkaProducer;

    @RequestMapping
    String home() {
        return "Hello World....- " + message + " " + appname;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public EmployeeDto create(@RequestBody EmployeeDto employee) {
        StringBuilder msg = new StringBuilder();
        msg.append("fullName=").append(employee.getFullName()).append(",");
        msg.append("email=").append(employee.getEmail()).append(",");
        msg.append("managerEmail=").append(employee.getManagerEmail());
        kafkaProducer.send(msg.toString());

        return employee;
    }
}

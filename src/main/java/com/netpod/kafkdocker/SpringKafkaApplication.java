package com.netpod.kafkdocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaApplication {
    public static void main(String[] args) {
        System.setProperty("advertised.host.name", "127.0.0.1");
        SpringApplication.run(SpringKafkaApplication.class, args);
    }
}

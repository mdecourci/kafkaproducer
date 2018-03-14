package com.netpod.kafkdocker.services;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaCountDownLatch {
    private CountDownLatch latch = new CountDownLatch(1);
    public CountDownLatch getLatch() {
        return latch;
    }
    public void countDown() {
        latch.countDown();;
    }

}

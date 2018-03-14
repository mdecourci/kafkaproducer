package com.netpod.kafkdocker;

import com.netpod.kafkdocker.services.KafkaConsumer;
import com.netpod.kafkdocker.services.KafkaCountDownLatch;
import com.netpod.kafkdocker.services.ListKafkaConsumer;
import com.netpod.kafkdocker.services.KafkaProducer;
import org.junit.ClassRule;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTest {

    private static String BOOT_TOPIC = "acxTopic";

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaCountDownLatch kafkaCountDownLatch;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 2, BOOT_TOPIC);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
    }

    @Test
    public void testKafka() throws Exception {
        kafkaProducer.send("Hello Boot!");

        kafkaCountDownLatch.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(kafkaCountDownLatch.getLatch().getCount(), is(0l));

    }

}

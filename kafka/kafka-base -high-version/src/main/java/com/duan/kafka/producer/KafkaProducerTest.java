package com.duan.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerTest {
    //http://kafka.apache.org/23/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "duan1:9092,duan2:9092,duan3:9092");
        props.put("transactional.id", "my-transactional-id");
        Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());

        producer.initTransactions();

        try {
            producer.beginTransaction();
            int j = 0;
            for (int i = 0; i < 100; i++) {
                producer.send(new ProducerRecord<>("bar", Integer.toString(i), "hello--"+Integer.toString(i)));
                j++;
                System.out.println("我是里："+i);
                Thread.sleep(500);
            }
            System.out.println("我是外："+j);
            producer.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            producer.close();
        }
        producer.close();
    }
}

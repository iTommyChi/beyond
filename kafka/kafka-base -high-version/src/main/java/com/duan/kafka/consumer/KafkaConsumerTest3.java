package com.duan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


/**
 * 测试消费者poll的数据业务处理时间超过kafka的max.poll.interval.ms时候，会发生什么事情
 */
public class KafkaConsumerTest3 {


    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "duan1:9092,duan2:9092,duan3:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //测试消费者poll的数据业务处理时间超过kafka的max.poll.interval.ms时候，会发生什么事情--start
        props.setProperty("max.poll.interval.ms", "1000");
        //测试消费者poll的数据业务处理时间超过kafka的max.poll.interval.ms时候，会发生什么事情--end


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("bar"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                /*try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}

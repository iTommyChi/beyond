package com.duan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


/**
 * 制造消费者poll的数据业务处理时间超过kafka的max.poll.interval.ms时候，出现了重复消费的现象
 */
public class KafkaConsumerTest2 {


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


        /**
         * 官方对 max.poll.interval.ms 参数的解释： https://kafka.apache.org/21/documentation.html
         * The maximum delay between invocations of poll() when using consumer group management.
         * This places an upper bound on the amount of time that the consumer can be idle before
         * fetching more records. If poll() is not called before expiration of this timeout,
         * then the consumer is considered failed and the group will rebalance in order to
         * reassign the partitions to another member.
         *
         * 使用使用者组管理时，调用poll（）之间的最大延迟。
         * 这为使用者在获取更多记录之前可以处于空闲状态的时间设置了上限。
         * 如果在此超时到期前未调用poll（），则认为使用方失败，
         * 该组将重新平衡以将分区重新分配给另一个成员。
         *
         */

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("bar"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
            System.out.println("records数量："+records.count());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

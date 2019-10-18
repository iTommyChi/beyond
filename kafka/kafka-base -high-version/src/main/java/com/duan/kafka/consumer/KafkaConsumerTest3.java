package com.duan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


/**
 * 演示自动提交出现数据丢失的现象
 * 演示前，先用生产者生产些数据出来，十来条数据即可
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


     /**
      * https://www.bbsmax.com/A/D854n76VzE/
      * 消费者将消费一批消息并将它们存储在内存中。当积累足够多的消息后再将它们批量插入到数据库中。
      * 如果设置offset自动提交，消费将被认为是已消费的，这样会出现问题，进程可能在批处理记录之后，
      * 但在它们被插入到数据库之前失败了。为了避免这种情况，我们将在相应的记录插入数据库之后再手动提交偏移量。
      * 这样我们可以准确控制消息是成功消费的。提出一个相反的可能性：在插入数据库之后，但是在提交之前，
      * 这个过程可能会失败（即使这可能只是几毫秒，这是一种可能性）。在这种情况下，进程将获取到已提交的偏移量，
      * 并会重复插入的最后一批数据。这种方式就是所谓的“至少一次”保证，在故障情况下，可以重复。
      * 如果您无法执行这些操作，可能会使已提交的偏移超过消耗的位置，从而导致缺少记录。
      * 使用手动偏移控制的优点是，您可以直接控制记录何时被视为“已消耗”。
      *
      * 注意：使用自动提交也可以“至少一次”。但是要求你必须下次调用poll（long）之前或关闭消费者之前，
      * 处理完所有返回的数据。如果操作失败，这将会导致已提交的offset超过消费的位置，从而导致丢失消息。
      * 使用手动控制offset的有点是，你可以直接控制消息何时提交。、
      */


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("bar"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            int count = records.count();
            System.out.println("count: "+count);
            for (ConsumerRecord<String, String> record : records){
                /**
                 * 理论上这里已经拿到了一批数据，offset会记录到这一批数据，认为已经消费了这一批数据了，
                 * 这时如果设置了自动提交并且在这个循环遍历里面出现了异常，是会造成数据丢失的
                 */
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

                //制造对批数据处理过程中，出现异常
                count --;
                if (count == 5){
                    try {
                        int a = 10/0;
                    }catch (Exception e){
                        break;
                    }
                }
            }

        }
    }
}

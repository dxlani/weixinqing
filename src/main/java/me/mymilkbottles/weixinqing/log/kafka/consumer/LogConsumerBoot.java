package me.mymilkbottles.weixinqing.log.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class LogConsumerBoot implements InitializingBean {

    @Value("${kafka.topic.log}")
    private String logTopicName;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();

        props.put("bootstrap.servers", "mini01:9092");

        //消费者的组id
        props.put("group.id", "GroupA");//这里是GroupA或者GroupB

        //从poll(拉)的回话处理时长
        props.put("session.timeout.ms", "7000");

        //poll的数量限制
        //props.put("max.poll.records", "100");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("enable.auto.commit", "false");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        //订阅主题列表topic
        kafkaConsumer.subscribe(Arrays.asList(logTopicName));

        new Thread(new LogKafkaConsumer(kafkaConsumer)).start();
    }
}

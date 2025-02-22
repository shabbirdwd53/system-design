package com.dailycodebuffer.system_design.ConsistentHashing.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.function.Consumer;

public class KafkaUtil {
    private static final String BROKER = "localhost:9092";
    private static final String TOPIC = "hash_ring_updates";

    public static void publish(String message) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.send(new ProducerRecord<>(TOPIC, message));
        }
    }

    public static void subscribe(Consumer<String> callback) {
        new Thread(() -> {
            Properties props = new Properties();
            props.put("bootstrap.servers", BROKER);
            props.put("group.id", "consistent-hashing-group");
            props.put("enable.auto.commit", "true");
            props.put("auto.offset.reset", "earliest");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    callback.accept(record.value());
                }
            }
        }).start();
    }
}

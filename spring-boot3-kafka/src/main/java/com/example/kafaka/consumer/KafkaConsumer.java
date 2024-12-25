package com.example.kafaka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * KafkaConsumer 类负责监听 Kafka 主题并处理接收到的消息。
 * 它使用 Spring Kafka 的 @KafkaListener 注解来定义监听器。
 */
@Service
public class KafkaConsumer {

    /**
     * 使用 @KafkaListener 注解定义一个 Kafka 监听器，监听 "test-topic" 主题，消费组为 "test-group"。
     * 当有消息到达主题时，此方法会被自动调用。
     *
     * @param message 从 Kafka 主题接收到的消息内容。
     */
    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) throws InterruptedException {
        System.out.println("Received message: " + message);
        Thread.sleep(100);
    }
}

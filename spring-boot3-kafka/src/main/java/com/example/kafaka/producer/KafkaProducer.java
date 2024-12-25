package com.example.kafaka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * KafkaProducer 类负责将消息发送到 Kafka 主题。
 * 它使用 Spring Kafka 的 KafkaTemplate 来实现消息的发送。
 */
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息到指定的 Kafka 主题。
     *
     * @param message 要发送的消息内容。
     */
    public void sendMessage(String message) {
        // 使用 KafkaTemplate 的 send 方法将消息发送到 "test-topic" 主题
        kafkaTemplate.send("test-topic", message);
    }
}

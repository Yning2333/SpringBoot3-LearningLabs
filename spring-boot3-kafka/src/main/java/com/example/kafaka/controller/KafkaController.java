package com.example.kafaka.controller;

import com.example.kafaka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * KafkaController 类负责处理与 Kafka 相关的 HTTP 请求。
 * 它通过调用 KafkaProducer 来发送消息到 Kafka 主题。
 */
@RestController
public class KafkaController {

    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * 处理发送 Kafka 消息的 HTTP GET 请求。
     *
     * @param message 要发送到 Kafka 主题的消息内容。
     * @return 一个字符串，表示消息已成功发送到 Kafka 主题。
     */
    @GetMapping("/sendKafkaMessage")
    public String sendKafkaMessage(@RequestParam String message) {
        // 调用 KafkaProducer 的 sendMessage 方法发送消息
        for (int i = 0; i <= 10000; i++) {
            kafkaProducer.sendMessage(message + i);
        }
        // 返回一个响应给客户端，表示消息已成功发送
        return "Message sent to Kafka topic";
    }
}

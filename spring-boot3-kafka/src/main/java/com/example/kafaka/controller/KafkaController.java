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
     * URL_ADDRESS:
     * 发送 Kafka 消息的 HTTP 接口。
     * http://127.0.0.1:8080/sendKafkaMessage?message=testMessage
     *
     * @param message 要发送到 Kafka 主题的基础消息内容。
     * @return 一个字符串，指示消息已成功发送到 Kafka。
     */
    @GetMapping("/sendKafkaMessage")
    public String sendKafkaMessage(@RequestParam String message) {
        // 循环发送 10000 条消息到 Kafka 主题，消息内容为 message 加上递增编号
        for (int i = 1; i <= 10000; i++) {
            kafkaProducer.sendMessage(message + i);
        }
        // 返回成功消息
        return "Messages sent to Kafka topic successfully.";
    }
}

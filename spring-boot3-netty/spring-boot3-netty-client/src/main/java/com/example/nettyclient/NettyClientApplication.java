package com.example.nettyclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 启用定时任务
public class NettyClientApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NettyClientApplication.class, args);
        try {
            NettyClient nettyClient = context.getBean(NettyClient.class);
            nettyClient.start();
        } catch (Exception e) {
            System.err.println("Netty client failed to start: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
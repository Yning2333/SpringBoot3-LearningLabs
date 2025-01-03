package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${spring.version}")
    private String springVersion;

    /**
     * 根路径映射方法。
     * URL_ADDRESS: http://127.0.0.1:8080/
     *
     * @return 返回一个包含欢迎信息和 Spring Boot 版本号的字符串。
     */
    @RequestMapping("/")
    public String index() {
        return new StringBuffer("Hello Spring Boot ").append(springVersion).toString();
    }
}

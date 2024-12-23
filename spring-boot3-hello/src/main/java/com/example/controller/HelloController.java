package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${spring.version}")
    private String springVersion;

    @RequestMapping("/")
    public String index() {
        return new StringBuffer("Hello Spring Boot ").append(springVersion).toString();
    }
}

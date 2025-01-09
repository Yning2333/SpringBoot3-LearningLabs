package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenapiApplication.class, args);
        System.out.println("Swagger UI:http://localhost:8080/swagger-ui.html");
        System.out.println("API 文档 JSON 格式:http://localhost:8080/v3/api-docs");
    }

}

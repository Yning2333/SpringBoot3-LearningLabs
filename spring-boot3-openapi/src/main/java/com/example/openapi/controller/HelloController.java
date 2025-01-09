package com.example.openapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例控制器，测试 OpenAPI 集成。
 */
@Tag(name = "Hello Controller", description = "Hello相关的 API")
@RestController()
public class HelloController {

    /**
     * 获取欢迎信息。
     *
     * @return 欢迎信息
     */
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Springdoc OpenAPI!";
    }

    /**
     * 获取带有参数的欢迎信息。
     *
     * @param name 用户名字
     * @return 欢迎信息
     */
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    /**
     * 获取带有查询参数的欢迎信息。
     *
     * @param name 用户名字
     * @param age 用户年龄
     * @return 欢迎信息
     */
    @GetMapping("/hello/age")
    public String sayHelloWithAge(@RequestParam String name, @RequestParam int age) {
        return "Hello, " + name + ". You are " + age + " years old.";
    }
}

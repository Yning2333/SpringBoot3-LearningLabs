package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-druid")
    public String testDruid() {
        // 执行简单的查询
        return jdbcTemplate.queryForObject("SELECT 'Druid Integration Successful' AS message", String.class);
    }
}
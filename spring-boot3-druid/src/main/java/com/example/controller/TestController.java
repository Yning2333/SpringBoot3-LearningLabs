package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/test-druid
     *
     * 测试 Druid 数据源的连接是否成功
     * @return 查询结果，固定返回 "Druid Integration Successful"
     */
    @GetMapping("/test-druid")
    public String testDruid() {
        // 执行简单的查询
        return jdbcTemplate.queryForObject("SELECT 'Druid Integration Successful' AS message", String.class);
    }
}
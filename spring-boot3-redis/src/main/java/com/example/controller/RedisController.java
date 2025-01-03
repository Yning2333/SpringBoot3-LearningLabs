package com.example.controller;

import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * URL_ADDRESS
     *
     * GET  http://127.0.0.1:8080/setRedisValue?key=name&value=123456
     *
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/setRedisValue")
    public String setRedisValue(@RequestParam(name = "key") String key, @RequestParam(name = "value") String value) {
        redisUtil.set(key, value);
        return "Value set successfully";
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/getRedisValue?key=name
     *
     * @param key
     * @return
     */
    @GetMapping("/getRedisValue")
    public String getRedisValue(@RequestParam(name = "key") String key) {
        return redisUtil.get(key);
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/deleteRedisValue?key=name
     *
     * @param key
     */
    @GetMapping("/deleteRedisValue")
    public void deleteRedisValue(@RequestParam(name = "key") String key) {
        redisUtil.delete(key);
    }
}

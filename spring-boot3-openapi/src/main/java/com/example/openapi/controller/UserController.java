package com.example.openapi.controller;

import com.example.openapi.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "User相关的 API")
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return new User("John Doe", 30);
    }

    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 用户对象
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return user;
    }
}

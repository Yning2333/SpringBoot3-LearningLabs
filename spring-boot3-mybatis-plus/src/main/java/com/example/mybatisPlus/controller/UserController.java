package com.example.mybatisPlus.controller;

import com.example.mybatisPlus.entity.User;
import com.example.mybatisPlus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        userService.save(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.updateById(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.removeById(id);
    }
}
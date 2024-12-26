package com.example.mongodb.controller;

import com.example.mongodb.model.User;
import com.example.mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * URL_ADDRESS GET http://localhost:8080/users
     *
     * @return
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
    /**
     * URL_ADDRESS GET http://localhost:8080/users/{id}
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    /**
     * URL_ADDRESS POST http://localhost:8080/users
     * Body (JSON):
     * {
     *   "name": "John Doe",
     *   "email": "john.doe@example.com"
     * }
     *
     * @param user
     * @return
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * URL_ADDRESS PUT  http://localhost:8080/users/{id}
     * {
     *   "name": "John Smith",
     *   "email": "john.smith@example.com"
     * }
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /**
     * URL_ADDRESS DELETE http://localhost:8080/users/{id}
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
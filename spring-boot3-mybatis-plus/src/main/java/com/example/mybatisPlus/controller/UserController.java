package com.example.mybatisPlus.controller;

import com.example.mybatisPlus.entity.User;
import com.example.mybatisPlus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // 自动注入 UserService 服务，UserService 是对 User 实体进行操作的服务层
    @Autowired
    private UserService userService;

    /**
     * 获取所有用户信息
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users
     *
     * @return 返回所有用户的列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.list();  // 使用 MyBatis-Plus 提供的 list() 方法获取所有用户
    }

    /**
     * 添加一个新的用户
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users
     *
     * @param user 要添加的用户信息，使用 @RequestBody 注解接收 JSON 格式的数据
     */
    @PostMapping
    public void addUser(@RequestBody User user) {
        userService.save(user);  // 使用 MyBatis-Plus 提供的 save() 方法保存用户信息
    }

    /**
     * 更新已有用户信息
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users
     *
     * @param user 要更新的用户信息，使用 @RequestBody 注解接收 JSON 格式的数据
     */
    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.updateById(user);  // 使用 MyBatis-Plus 提供的 updateById() 方法更新用户信息
    }

    /**
     * 删除用户
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users/{id}
     *
     * @param id 用户的 ID，使用 @PathVariable 注解从 URL 路径中获取用户的 ID
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.removeById(id);  // 使用 MyBatis-Plus 提供的 removeById() 方法删除指定 ID 的用户
    }
}

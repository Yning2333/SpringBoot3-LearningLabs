package com.example.mybatis.controller;

import com.example.mybatis.entity.User;
import com.example.mybatis.mapper.UserMapper;
import com.example.mybatis.mapper.UserXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserXmlMapper userXmlMapper;

    /**
     * 获取所有用户（基于注解方式）。
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users/annotation
     *
     * @return 包含所有用户的列表。
     */
    @GetMapping("/annotation")
    public List<User> getAllUsersWithAnnotation() {
        return userMapper.findAll();
    }

    /**
     * 获取所有用户（基于 XML 配置方式）。
     * URL_ADDRESS:
     * http://127.0.0.1:8080/users/xml
     *
     * @return 包含所有用户的列表。
     */
    @GetMapping("/xml")
    public List<User> getAllUsersWithXml() {
        return userXmlMapper.findAll();
    }
}

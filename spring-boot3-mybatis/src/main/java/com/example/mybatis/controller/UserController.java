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

    @GetMapping("/annotation")
    public List<User> getAllUsersWithAnnotation() {
        return userMapper.findAll();
    }

    @GetMapping("/xml")
    public List<User> getAllUsersWithXml() {
        return userXmlMapper.findAll();
    }
}

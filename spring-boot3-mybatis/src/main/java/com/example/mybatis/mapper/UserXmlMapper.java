package com.example.mybatis.mapper;

import com.example.mybatis.entity.User;
import java.util.List;

public interface UserXmlMapper {

    List<User> findAll();

    void insert(User user);

    void update(User user);

    void delete(Long id);
}

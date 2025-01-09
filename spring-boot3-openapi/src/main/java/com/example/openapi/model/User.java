package com.example.openapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户数据传输对象")
public class User {

    @Schema(description = "姓名", example = "John Doe")
    private String name;

    @Schema(description = "年龄", example = "30")
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

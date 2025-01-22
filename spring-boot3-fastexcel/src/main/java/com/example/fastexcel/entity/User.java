package com.example.fastexcel.entity;

import cn.idev.excel.annotation.ExcelProperty;

public class User {
    @ExcelProperty("编号")
    private Integer id;
    @ExcelProperty("名字")
    private String name;
    @ExcelProperty("年龄")
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

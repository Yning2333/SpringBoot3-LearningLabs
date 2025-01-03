package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * 处理访问公开页面的请求。
     *
     * URL_ADDRESS:
     * http://127.0.0.1:8080/public
     *
     * @return 返回 public 页面视图
     */
    @GetMapping("/public")
    public String publicPage() {
        return "public";  // 返回 public 页面视图
    }

    /**
     * 处理访问主页的请求，主页是经过身份验证的用户才可以访问的页面。
     *
     * URL_ADDRESS:
     * http://127.0.0.1:8080/home
     *
     * @return 返回 home 页面视图
     */
    @GetMapping("/home")
    public String homePage() {
        return "home";  // 返回 home 页面视图
    }

    /**
     * 处理访问登录页面的请求。
     *
     * URL_ADDRESS:
     * http://127.0.0.1:8080/login
     *
     * @return 返回 login 页面视图
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // 返回 login 页面视图
    }
}

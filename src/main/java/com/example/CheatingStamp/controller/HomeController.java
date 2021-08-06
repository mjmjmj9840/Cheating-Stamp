package com.example.CheatingStamp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // 홈(시작) 페이지
    @GetMapping("/")
    public String home() {
        return "index";
    }
}

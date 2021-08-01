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

    // 아이트래킹 보정 페이지
    @GetMapping("/calibration")
    public String calibration() {
        return "calibration";
    }

    // 시험 페이지
    @GetMapping("/exam")
    public String exam() {
        return "exam";
    }
}

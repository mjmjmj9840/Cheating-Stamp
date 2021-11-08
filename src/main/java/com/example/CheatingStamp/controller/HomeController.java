package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.model.UserRole;
import com.example.CheatingStamp.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // 홈(시작) 페이지
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (userDetails != null) {  // 로그인한 유저의 경우
            User user = userDetails.getUser();
            if (user.getRole() == UserRole.SUPERVISOR) {
                model.addAttribute("supervisor", true);
            }
        }
        return "index";
    }
}

package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CalibrationRateRequestDto;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.security.UserDetailsImpl;
import com.example.CheatingStamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class CalibrationController {

    private final UserService userService;

    // 아이트래킹 보정 페이지
    @GetMapping("/calibration")
    public String calibration(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "calibration";
    }

    @PostMapping("/calibration")
    public String saveCalibrationRate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CalibrationRateRequestDto requestDto) {
        User user = userDetails.getUser();
        userService.updateCalibrationRate(user, requestDto);
        return "redirect:/";
    }
}

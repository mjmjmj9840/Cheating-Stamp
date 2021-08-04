package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CalibrationRateRequestDto;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.security.UserDetailsImpl;
import com.example.CheatingStamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public void saveCalibrationRate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam CalibrationRateRequestDto requestDto) {
        Long userId = userDetails.getUser().getId();
        userService.updateCalibrationRate(userId, requestDto);
    }
}

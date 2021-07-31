package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.SignupRequestDto;
import com.example.CheatingStamp.service.UserService;
import com.example.CheatingStamp.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 로그인 실패 페이지
//    @GetMapping("/login-error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "login";
//    }

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("signupRequestDto",new SignupRequestDto());
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/signup")
    public String register(@Valid SignupRequestDto signupRequestDto, BindingResult bindingResult){
        // @Valid로 객체 입증
        // BindingReslult로 에러가 있다면, 해당 에러 메세지를 (key, value) 형태로 전달 후 /signup 재호출
        userValidator.validate(signupRequestDto, bindingResult);    // 중복 검사
        if(bindingResult.hasErrors()) {
            return "signup"; // 실패
        }
        else {
            // 성공
            userService.registerUser(signupRequestDto);
            return "redirect:/login";
        }
    }
}
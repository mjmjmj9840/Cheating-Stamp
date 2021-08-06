package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.service.ExamService;
import com.example.CheatingStamp.validator.ExamValidator;
import com.example.CheatingStamp.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.tools.jconsole.JConsole;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class ExamController {

    private final ExamService examService;
    private final ExamValidator examValidator;

    // 시험 생성 페이지
    @GetMapping("/TESTexam")
    public String setExam(Model model){
        model.addAttribute("createExamRequestDto",new CreateExamRequestDto());
        return "TESTexam";
    }

    @PostMapping("/TESTexam")
    public String createExam(CreateExamRequestDto createExamRequestDto, BindingResult bindingResult){
        //examValidator.validate(createExamRequestDto, bindingResult);    // 날짜 유효성 검사

        examService.createExam(createExamRequestDto);
        return "redirect:/exam";
    }
}
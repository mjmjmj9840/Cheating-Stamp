package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.service.ExamService;
import com.example.CheatingStamp.validator.ExamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.CheatingStamp.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class ExamController {

    private final ExamService examService;
    private final ExamValidator examValidator;

    // 시험 생성 페이지
    @GetMapping("/TESTexam")
    public String setExam(Model model) {
        model.addAttribute("createExamRequestDto", new CreateExamRequestDto());
        return "TESTexam";
    }

    @PostMapping("/TESTexam")
    public String createExam(CreateExamRequestDto createExamRequestDto, BindingResult bindingResult) {
        //examValidator.validate(createExamRequestDto, bindingResult);    // 날짜 유효성 검사

        examService.createExam(createExamRequestDto);
        return "redirect:/exam";
    }

    // 응시자와 시험 정보 빼오는 부분 서비스로 분리하기
    // 시험 대기 화면
    @GetMapping("/waiting")
    public String waiting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long userId = userDetails.getUser().getId();
        // 예정된 시험이 없을 경우 홈 화면으로 넘김(시험 DB 완성 후 수정)

        // 아이트래킹 보정 전일 경우 보정 화면으로 넘김
        int calibrationRate = userDetails.getUser().getCalibrationRate();
        ;
        if (calibrationRate < 50) {
            model.addAttribute("doesNotCalibrate", true);
            return "redirect:/calibration";
        }


        // 시작시간, 종료시간, 시험제목, 시험코드 빼오기
        // 현재는 임의의 값(시험 DB 완성 후 수정)
        LocalDateTime examStartTime = LocalDateTime.now().plusMinutes(30);  // 30분 뒤 시험 시작
        LocalDateTime examEndTime = LocalDateTime.now().plusHours(1);  // 1시간 뒤 시험 종료
        String examTitle = "소프트웨어 공학^^*";
        String examCode = "sldkj2349djfkln23kn4bl9";

        model.addAttribute("examStartTime", examStartTime);
        model.addAttribute("examEndTime", examEndTime);
        model.addAttribute("examTitle", examTitle);
        model.addAttribute("examCode", examCode);

        return "waiting";
    }

    // 시험 화면
    @GetMapping("/exam/{code}")
    public String exam(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String code, Model model) {
        // 시험ID, 시작시간, 종료시간 빼오기(시험 DB 완성 후 수정)
        Long examId = 1L;
        LocalDateTime examStartTime = LocalDateTime.now().minusMinutes(10);  // 10분 전 시험 시작
        LocalDateTime examEndTime = LocalDateTime.now().plusHours(1);  // 1시간 뒤 시험 종료
        LocalDateTime now = LocalDateTime.now();
        // 시험 시작 전일 경우 대기 화면으로 넘김
        if (now.isBefore(examStartTime)) {
            return "redirect:/waiting";
        }
        // 시험 종료 후엔 접근할 수 없음
        if (now.isAfter(examEndTime)) {
            return "redirect:/";
        }


        // 시험제목, 시험문제 빼오기(시험 DB 완성 후 수정)
        String examTitle = "소프트웨어 공학";
        List<String> questionList = new ArrayList<>();
        questionList.add("1번 문제~~~");
        model.addAttribute("examId", examId);
        model.addAttribute("examEndTime", examEndTime);
        model.addAttribute("examTitle", examTitle);
        model.addAttribute("questionList", questionList);

        return "exam";
    }
}


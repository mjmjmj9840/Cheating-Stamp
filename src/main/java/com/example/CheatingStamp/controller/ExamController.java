package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.service.ExamService;
import com.example.CheatingStamp.service.S3Service;
import com.example.CheatingStamp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.CheatingStamp.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@RequiredArgsConstructor
@Controller
public class ExamController {

    private final ExamService examService;
    private final S3Service s3Service;
    private final VideoService videoService;

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
        return "redirect:/";
    }

    // 응시자와 시험 정보 빼오는 부분 서비스로 분리하기
    // 시험 대기 화면
    @GetMapping("/waiting")
    public String waiting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long userId = userDetails.getUser().getId();
        // 예정된 시험이 없을 경우 홈 화면으로 넘김(시험 DB 완성 후 수정)

        // 아이트래킹 보정 전일 경우 보정 화면으로 넘김
        int calibrationRate = userDetails.getUser().getCalibrationRate();

        if (calibrationRate < 50) {
            model.addAttribute("doesNotCalibrate", true);
            return "calibration";
        }

        // 가장 가까운 시험의 정보 받아오기
        Long examId = examService.getFirstExamId(userId);
        HashMap<String,String> infoMap = examService.getExamInfo(examId);

        model.addAttribute("examStartTime", infoMap.get("examStartTime"));  // yyyyMMddHHmm
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));  // yyyyMMddHHmm
        model.addAttribute("examTime", infoMap.get("examTime"));  // mm분
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        model.addAttribute("examCode", infoMap.get("examCode"));

        return "waiting";
    }

    // 시험 화면
    @GetMapping("/exam/{code}")
    public String exam(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String code, Model model) {
        // 시험 코드에 해당하는 시험 정보 받아오기
        Long examId = examService.getExamIdByCode(code);
        HashMap<String,String> infoMap = examService.getExamInfo(examId);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        // 시험 시작 전일 경우 대기 화면으로 넘김
        if (now.compareTo(infoMap.get("examStartTime")) < 0) {
            return "redirect:/waiting";
        }
        // 시험 종료 후엔 접근할 수 없음
        if (now.compareTo(infoMap.get("examEndTime")) > 0) {
            return "redirect:/";
        }

        model.addAttribute("examId", examId);
        model.addAttribute("examTime", infoMap.get("examTime"));
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        // model.addAttribute("questionList", questionList);

        return "exam";
    }

    // 임시 시험 화면
    @GetMapping("/exam")
    public String exam(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "exam";
    }

    // 녹화 테스트 화면
    @GetMapping("/TESTrecord")
    public String testRecord(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "TESTrecord";
    }

    // 응시 영상 업로드
    @PostMapping("/upload")
    public String uploadVideo(@AuthenticationPrincipal UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        // s3에 응시 영상 업로드
        String title = userDetails.getUser().getUsername();
        String filePath = s3Service.upload(file, title);
        // DB에 영상 이름과 url 저장
        VideoRequestDto requestDto = new VideoRequestDto();
        requestDto.setTitle(title);
        requestDto.setFilePath(filePath);

        videoService.savePost(requestDto);

        return "redirect:/examEnd";
    }

    // 시험 종료 화면
    @GetMapping("/examEnd")
    public String examEnd(Model model) {
        return "examEnd";
    }
}


package com.example.CheatingStamp.controller;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.dto.ExamUserRequestDto;
import com.example.CheatingStamp.dto.SaveAnswerRequestDto;
import com.example.CheatingStamp.dto.VideoRequestDto;
import com.example.CheatingStamp.service.ExamService;
import com.example.CheatingStamp.service.AnswerService;
import com.example.CheatingStamp.service.S3Service;
import com.example.CheatingStamp.service.VideoService;
import com.example.CheatingStamp.service.*;
import com.example.CheatingStamp.model.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.CheatingStamp.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class ExamController {

    private final ExamService examService;
    private final AnswerService answerService;
    private final UserService userService;
    private final S3Service s3Service;
    private final VideoService videoService;
    private final ExamUserService examUserService;

    // 시험 생성 페이지
    @GetMapping("/createExam")
    public String createExam(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "createExam";
    }

    @ResponseBody
    @PostMapping("/createExam")
    public String saveExam(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute CreateExamRequestDto requestDto) {
        Long userId = userDetails.getUser().getId();
        examService.createExam(requestDto, userId);

        return "redirect:/index";
    }

    // 시험 대기 화면
    @GetMapping("/waiting")
    public String waiting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long examId = userService.getFirstExamId(userDetails.getUser());
        // 예정된 시험이 없을 경우 홈 화면으로 넘김
        if (examId < 0) {
            model.addAttribute("noExam", true);
            return "index";
        }

        // 아이트래킹 보정 전일 경우 보정 화면으로 넘김
        int calibrationRate = userDetails.getUser().getCalibrationRate();

        if (calibrationRate < 50) {
            model.addAttribute("doesNotCalibrate", true);
            return "calibration";
        }

        // 가장 가까운 시험의 정보 받아오기
        HashMap<String,String> infoMap = examService.getExamInfo(examId);

        model.addAttribute("examStartTime", infoMap.get("examStartTime"));  // yyyyMMddHHmm
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));  // yyyyMMddHHmm
        model.addAttribute("examTime", infoMap.get("examTime"));  // mm분
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        model.addAttribute("examCode", infoMap.get("examCode"));

        return "waiting";
    }
    
    // 시험 화면
    @GetMapping("/exam")
    public String exam(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String code, Model model) {
        // 시험 코드에 해당하는 시험 정보 받아오기
        Long examId = examService.getExamIdByCode(code);
        HashMap<String, String> infoMap = examService.getExamInfo(examId);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        // 시험 시작 전일 경우 대기 화면으로 넘김
        if (now.compareTo(infoMap.get("examStartTime")) < 0) {
            return "redirect:/waiting";
        }
        // 시험 종료 후엔 접근할 수 없음
        if (now.compareTo(infoMap.get("examEndTime")) > 0) {
            model.addAttribute("endExam", true);
            return "index";
        }

        model.addAttribute("examId", examId);
        model.addAttribute("examCode", infoMap.get("examCode"));
        model.addAttribute("examTime", infoMap.get("examTime"));
        model.addAttribute("examStartTime", infoMap.get("examStartTime"));
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        model.addAttribute("examQuestions", infoMap.get("examQuestions"));

        return "exam";
    }

    @ResponseBody
    @PostMapping("/exam/{code}")
    public String saveAnswer(@PathVariable String code, @ModelAttribute SaveAnswerRequestDto requestDto) {
        answerService.createAnswer(requestDto);

        return "redirect:/examEnd";
    }

    // 응시 영상 업로드
    @PostMapping("/upload/{code}")
    public String uploadVideo(@PathVariable String code, @AuthenticationPrincipal UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        // s3에 응시 영상 업로드
        String username = userDetails.getUser().getUsername();
        String filePath = s3Service.upload(file, code + "_" + username);  // 영상 제목: 시험 코드_응시자 이메일
        // DB에 영상 이름과 url 저장
        VideoRequestDto requestDto = new VideoRequestDto();
        requestDto.setUsername(username);
        requestDto.setFilePath(filePath);

        examService.addVideoByExamCode(videoService.savePost(requestDto), code);  // 영상 저장 후 exam과 연결

        return "redirect:/examEnd";
    }

    // 시험 종료 화면
    @GetMapping("/examEnd")
    public String examEnd(Model model) {
        return "examEnd";
    }

  
    // ======= 감독관용 화면 =======
    // 시험 관리 페이지
    @GetMapping("/settingExam")
    public String examSetting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        // 권한을 가진 유저인지 확인
        User user = userDetails.getUser();
        if (user.getRole().name() != "SUPERVISOR"){
            return "redirect:/index";
        }

        // 유저가 관리하는 시험 정보 받아오기
        Long managerId = user.getId();
        List<HashMap<String, String>> examList = examService.getExamByManagerId(managerId);

        model.addAttribute("examList", examList);

        return "settingExam";
    }

    @ResponseBody
    @PostMapping("/settingExam")
    public String examDelete(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value="checkedExam") List<Long> checkedExam) {
        examUserService.deleteByExamIds(checkedExam);
        examService.deleteExamByExamIds(checkedExam);
        return "redirect:/settingExam";
    }

    // 시험 상세 화면
    @GetMapping("/detailExam")
    public String examDetail(@RequestParam Long examId, Model model) {
        HashMap<String,String> infoMap = examService.getExamInfo(examId);

        model.addAttribute("examId", examId);
        model.addAttribute("examStartTime", infoMap.get("examStartTime"));  // yyyyMMddHHmm
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));  // yyyyMMddHHmm
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        model.addAttribute("examCode", infoMap.get("examCode"));
        model.addAttribute("examQuestions", infoMap.get("examQuestions"));

        HashMap<String, List> userInfoMap = examUserService.getExamUsers(examId);
        model.addAttribute("supervisors", userInfoMap.get("supervisors"));
        model.addAttribute("testers", userInfoMap.get("testers"));

        return "detailExam";
    }

    @PostMapping("/detailExam")
    public String addExamUser(@RequestBody ExamUserRequestDto requestDto) {
        examUserService.addByExamIdAndUsername(requestDto);

        return "redirect:/";
    }

    @GetMapping("/deleteExam/{examId}/{username}")
    public String deleteExamUser(@PathVariable Long examId, @PathVariable String username) {
        examUserService.deleteByExamIdAndUsername(examId, username);

        return "redirect:/";
    }

    // 응시 영상 목록
    @GetMapping("/watchingList")
    public String watchingList(@RequestParam Long examId, Model model) {
        HashMap<String,String> infoMap = examService.getExamInfo(examId);

        model.addAttribute("examId", examId);
        model.addAttribute("examStartTime", infoMap.get("examStartTime"));
        model.addAttribute("examEndTime", infoMap.get("examEndTime"));
        model.addAttribute("examTitle", infoMap.get("examTitle"));
        model.addAttribute("examCode", infoMap.get("examCode"));

        JSONArray testerInfo = examUserService.getTestersInfo(examId);
        model.addAttribute("testerInfo", testerInfo);

        return "watchingList";
    }
}


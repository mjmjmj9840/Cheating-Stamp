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
import com.example.CheatingStamp.validator.UserValidator;
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
    private final UserValidator userValidator;

    // 시험 대기 화면
    @GetMapping("/waiting")
    public String waiting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long examId = userService.getFirstExamId(userDetails.getUser());
        // 예정된 시험이 없을 경우 홈 화면으로 넘김
        if (examId < 0) {
            model.addAttribute("errorMsg", "예정된 시험이 없습니다.");
            return "errorMsg";
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
        User user = userDetails.getUser();
        if (!examUserService.validationTesterByUserAndExamCode(user, code)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

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
            model.addAttribute("errorMsg", "해당 시험이 이미 종료되었습니다.");
            return "errorMsg";
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
    public String saveAnswer(@PathVariable String code, @AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute SaveAnswerRequestDto requestDto, Model model) {
        User user = userDetails.getUser();
        if (!examUserService.validationTesterByUserAndExamCode(user, code)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        String username = userDetails.getUser().getUsername();
        answerService.createAnswer(requestDto, username);

        return "redirect:/examEnd";
    }

    // 응시 영상 업로드
    @PostMapping("/upload/{code}")
    public String uploadVideo(@PathVariable String code, @AuthenticationPrincipal UserDetailsImpl userDetails, MultipartFile file, Model model) throws IOException {
        User user = userDetails.getUser();
        if (!examUserService.validationTesterByUserAndExamCode(user, code)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

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


    // ======= 모바일 화면 =======

    @GetMapping("/m")
    public String mobile(@RequestParam String code, Model model) {
        model.addAttribute("code", code);

        return "mHome";
    }

    @GetMapping("/mobileGuide")
    public String mobileGuide(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        Long examId = userService.getFirstExamId(user);
        // 예정된 시험이 없을 경우 홈 화면으로 넘김
        if (examId < 0) {
            model.addAttribute("errorMsg", "예정된 시험이 없습니다.");
            return "errorMsg";
        }

        // mobileUrl 전달
        String mobileUrl = examUserService.getMobileUrlByExamIdAndUserId(examId, user);
        model.addAttribute("mobileUrl", mobileUrl);

        return "mobileGuide";
    }

  
    // ======= 감독관용 화면 =======

    // 시험 관리 페이지
    @GetMapping("/settingExam")
    public String examSetting(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        // 유저가 관리하는 시험 정보 받아오기
        Long managerId = userDetails.getUser().getId();
        List<HashMap<String, String>> examList = examService.getExamByManagerId(managerId);

        model.addAttribute("examList", examList);

        return "settingExam";
    }

    @ResponseBody
    @PostMapping("/settingExam")
    public String examDelete(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value="checkedExam") List<Long> checkedExam, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        examUserService.deleteByExamIds(checkedExam);
        examService.deleteExamByExamIds(checkedExam);
        return "redirect:/settingExam";
    }

    // 시험 생성 페이지
    @GetMapping("/createExam")
    public String createExam(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }
        return "createExam";
    }

    @ResponseBody
    @PostMapping("/createExam")
    public String saveExam(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute CreateExamRequestDto requestDto, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        Long userId = userDetails.getUser().getId();
        examService.createExam(requestDto, userId);

        return "redirect:/settingExam";
    }

    // 시험 상세 화면
    @GetMapping("/detailExam")
    public String examDetail(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long examId, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

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
    public String addExamUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ExamUserRequestDto requestDto, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        examUserService.addByExamIdAndUsername(requestDto);

        return "redirect:/";
    }

    @GetMapping("/deleteExam/{examId}/{username}")
    public String deleteExamUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long examId, @PathVariable String username, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        examUserService.deleteByExamIdAndUsername(examId, username);

        return "redirect:/";
    }

    @GetMapping("/watchingVideo")
    public String watchingVideo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long videoId, @RequestParam Long mobileVideoId, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        HashMap<String, String> videoInfo = videoService.getVideoInfo(videoId);
        String mobileFilePath = null;
        if (mobileVideoId != -1) {  // 모바일 응시 영상이 있는 경우
            mobileFilePath = videoService.getMobileVideoFilePath(mobileVideoId);
        }

        if (!videoInfo.isEmpty()) {
            String username = videoInfo.get("username");
            Long examId = Long.valueOf(videoInfo.get("examId"));

            model.addAttribute("filePath", videoInfo.get("filePath"));
            model.addAttribute("mobileFilePath", mobileFilePath);

            model.addAttribute("examTitle", videoInfo.get("examTitle"));
            model.addAttribute("username", username);

            HashMap<String, List> timestampInfo = answerService.getTimestampByExamIdAndUsername(examId, username);
            model.addAttribute("timestamp", timestampInfo.get("timestamp"));

            return "watchingVideo";
        } else {
            model.addAttribute("errorMsg", "수험자의 응시 영상이 존재하지 않습니다.");
            return "errorMsg";
        }
    }

    // 응시 영상 목록
    @GetMapping("/watchingList")
    public String watchingList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long examId, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

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

    // 응시 영상 목록 삭제
    @GetMapping("/deleteWatchingList")
    public String watchingVideo(@RequestParam Long examId, @RequestParam String username, @RequestParam Long videoId) throws IOException {
        // 응시 영상 삭제
        HashMap<String, String> videoInfo = videoService.getVideoInfo(videoId);
        if (videoInfo.isEmpty()) {
            System.out.println("잘못된 video id 값입니다.");
        } else {
            HashMap<String, String> examInfo = examService.getExamInfo(examId);
            String examCode = examInfo.get("examCode");
            String videoTitle = examCode + "_" + username;

            boolean isDeleted = s3Service.delete(videoTitle);  // S3에서 video 삭제
            if (!isDeleted) {
                System.out.println("이미 S3에서 삭제된 video입니다.");
            }

            videoService.deleteVideo(videoId);  // DB에서 video 삭제
        }

        // 응시자 답안 삭제
        answerService.deleteAnswer(examId, username);

        // ExamUser 삭제
        examUserService.deleteByExamIdAndUsername(examId, username);

        return "redirect:/watchingList?examId=" + examId;
    }

    // 답안 확인
    @GetMapping("/checkAnswer")
    public String checkAnswer(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long examId, @RequestParam String username, Model model) {
        if (!userValidator.isSupervisor(userDetails)) {
            model.addAttribute("errorMsg", "권한이 없는 사용자입니다.");
            return "errorMsg";
        }

        HashMap<String, String> examInfo = examService.getExamInfo(examId);
        String answers = answerService.getAnswersByExamIdAndUsername(examId, username);
        String name = userService.getNameByUsername(username);

        model.addAttribute("examTitle", examInfo.get("examTitle"));
        model.addAttribute("examQuestions", examInfo.get("examQuestions"));
        model.addAttribute("name", name);
        model.addAttribute("email", username);
        model.addAttribute("answers", answers);

        return "checkAnswer";
    }
}


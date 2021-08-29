package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.model.ExamUser;
import com.example.CheatingStamp.model.User;
import com.example.CheatingStamp.model.UserRole;
import com.example.CheatingStamp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;

    // String 타입으로 받아온 startTime, timeout 변수를 LocalDateTime 타입으로 변경
    public LocalDateTime StringToTime(String string) {
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeEdited = LocalDateTime.parse(string, fomatter1);
        return timeEdited;
    }

    // requestDto로부터 정보 받아와 객체 생성
    public Long createExam(CreateExamRequestDto requestDto) {
        String title = requestDto.getTitle();
        String code = UUID.randomUUID().toString().replace("-", "");    // 고유식별자(UUID) 생성
        LocalDateTime starTime = StringToTime(requestDto.getStartTime());
        LocalDateTime endTime = StringToTime(requestDto.getEndTime());
        String questions = requestDto.getQuestions();

        Exam exam = new Exam(code, title, starTime, endTime, questions);
        examRepository.save(exam);

        return exam.getId();
    }

    public Long getExamIdByCode(String code) {
        Optional<Exam> found = examRepository.findByCode(code);
        if (!found.isPresent()) {
            throw new NullPointerException("해당 코드의 시험이 존재하지 않습니다.");
        }
        return found.get().getId();
    }

    public HashMap getExamInfo(Long id) {
        HashMap<String, String> infoMap = new HashMap<String, String>();
        Exam exam = examRepository.getById(id);
        // examCode
        String examCode = exam.getCode();
        infoMap.put("examCode", examCode);
        // examTitle
        String examTitle = exam.getTitle();
        infoMap.put("examTitle", examTitle);
        // examStartTime
        String examStartTime = exam.getStartTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        infoMap.put("examStartTime", examStartTime);
        // examEndTime
        String examEndTime = exam.getEndTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        infoMap.put("examEndTime", examEndTime);
        // examTime
        String examTime = ChronoUnit.MINUTES.between(exam.getStartTime(), exam.getEndTime()) + "분";
        infoMap.put("examTime", examTime);
        // examQuestions
        String examQuestions = exam.getQuestions();
        infoMap.put("examQuestions", examQuestions);

        return infoMap;
    }

    public HashMap getExamUsers(Long id) {
        HashMap<String, List> infoMap = new HashMap<String, List>();
        Exam exam = examRepository.getById(id);

        // supervisors, testers
        List<ExamUser> examUsers = exam.getExamUsers();
        List<String> supervisors = new ArrayList<>();
        List<String> testers = new ArrayList<>();
        for (int i = 0; i < examUsers.size(); i++) {
            User user = examUsers.get(i).getUser();
            if (user.getRole() == UserRole.SUPERVISOR)
                supervisors.add(user.getUsername());
            else
                testers.add(user.getUsername());
        }

        infoMap.put("supervisors", supervisors);
        infoMap.put("testers", testers);

        return infoMap;
    }
}
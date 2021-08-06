package com.example.CheatingStamp.service;

import com.example.CheatingStamp.dto.CreateExamRequestDto;
import com.example.CheatingStamp.model.Exam;
import com.example.CheatingStamp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;

    // String 타입으로 받아온 startTime, timeout 변수를 LocalDateTime 타입으로 변경
    public LocalDateTime StringToTime (String string) {
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeEdited = LocalDateTime.parse(string, fomatter1);
        return timeEdited;
    }

    // requestDto로부터 정보 받아와 객체 생성
    public void createExam(CreateExamRequestDto requestDto) {

        String title = requestDto.getTitle();
        String code = UUID.randomUUID().toString().replace("-", "");    // 고유식별자(UUID) 생성

        LocalDateTime starTime = StringToTime(requestDto.getStartTime());
        System.out.println(starTime);

        LocalDateTime endTime = StringToTime(requestDto.getEndTime());
        System.out.println(endTime);

        Exam exam = new Exam(code, title, starTime, endTime);
        examRepository.save(exam);
    }
}
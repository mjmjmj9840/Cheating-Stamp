package com.example.CheatingStamp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateExamRequestDto {
    // code : ExamService에서 생성
    private String title;
    // String -> LocalDateTime : ExamService에서 변환
    private String startTime;
    private String endTime;
}
